import { promises as fs } from 'fs';
import path from 'path';

const DATA_DIR = path.resolve(process.cwd(), 'data');
const DB_FILE = path.join(DATA_DIR, 'db.json');

export type Db = {
  registrations: Record<string, Record<string, unknown>>;
};

async function ensureDataFile() {
  await fs.mkdir(DATA_DIR, { recursive: true });
  try {
    await fs.access(DB_FILE);
  } catch {
    const initial: Db = { registrations: {} };
    await fs.writeFile(DB_FILE, JSON.stringify(initial, null, 2), 'utf-8');
  }
}

export async function loadDb(): Promise<Db> {
  await ensureDataFile();
  const raw = await fs.readFile(DB_FILE, 'utf-8');
  return JSON.parse(raw) as Db;
}

export async function saveDb(db: Db): Promise<void> {
  await ensureDataFile();
  await fs.writeFile(DB_FILE, JSON.stringify(db, null, 2), 'utf-8');
} 
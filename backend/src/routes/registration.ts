import { Router } from 'express';
import { requireAuth } from '../middleware/auth.js';
import { loadDb, saveDb } from '../storage/db.js';

const router = Router();

router.get('/:userId', requireAuth, async (req, res) => {
  const userId = req.params.userId;
  const db = await loadDb();
  res.json(db.registrations[userId] ?? {});
});

router.post('/:userId/:section', requireAuth, async (req, res) => {
  const userId = req.params.userId;
  const section = req.params.section;
  const data = req.body;
  const db = await loadDb();
  db.registrations[userId] = db.registrations[userId] || {};
  db.registrations[userId][section] = data;
  await saveDb(db);
  res.json({ ok: true });
});

export default router; 
import { Router } from 'express';
import bcrypt from 'bcryptjs';
import { signJwt, requireAuth, AuthRequest } from '../middleware/auth.js';

const router = Router();

// In-memory users (admin)
const adminEmail = process.env.ADMIN_EMAIL || 'admin@example.com';
const adminPassword = process.env.ADMIN_PASSWORD || 'admin123';
const adminHash = bcrypt.hashSync(adminPassword, 10);

const users = new Map<string, { id: string; email: string; passwordHash: string; role: 'ADMIN' | 'USER' }>([
  [adminEmail, { id: '1', email: adminEmail, passwordHash: adminHash, role: 'ADMIN' }]
]);

router.post('/login', (req, res) => {
  const { email, password } = req.body as { email: string; password: string };
  const user = users.get(email);
  if (!user) return res.status(401).json({ message: 'Invalid credentials' });
  const ok = bcrypt.compareSync(password, user.passwordHash);
  if (!ok) return res.status(401).json({ message: 'Invalid credentials' });
  const token = signJwt({ sub: user.id, email: user.email, role: user.role });
  res.json({ token });
});

router.get('/me', requireAuth, (req: AuthRequest, res) => {
  res.json({ user: req.user });
});

export default router; 
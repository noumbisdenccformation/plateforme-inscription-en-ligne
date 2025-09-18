import { Router } from 'express';
import { requireAuth, requireAdmin } from '../middleware/auth.js';

const router = Router();

router.get('/', requireAuth, requireAdmin, (req, res) => {
  const page = Number(req.query.page ?? 1);
  const pageSize = Number(req.query.pageSize ?? 20);
  const items = Array.from({ length: pageSize }).map((_, i) => {
    const id = (page - 1) * pageSize + i + 1;
    const states = ['PENDING', 'APPROVED', 'REJECTED'] as const;
    return {
      id,
      candidat: `Candidat ${id}`,
      completude: Math.floor(Math.random() * 100),
      etat: states[Math.floor(Math.random() * states.length)],
      deposeLe: new Date(Date.now() - Math.random() * 1e10).toISOString()
    };
  });
  res.json({ items, total: 200 });
});

export default router; 
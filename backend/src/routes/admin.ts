import { Router } from 'express';
import { requireAuth, requireAdmin } from '../middleware/auth.js';

const router = Router();

router.get('/dashboard', requireAuth, requireAdmin, (_req, res) => {
  res.json({
    totalInscriptions: 1280,
    pending: 312,
    approved: 842,
    rejected: 126,
    completionByStep: [78, 64, 52, 39, 21]
  });
});

router.get('/heatmap', requireAuth, requireAdmin, (_req, res) => {
  res.json([
    { day: 'Lun', value: 14 }, { day: 'Mar', value: 22 }, { day: 'Mer', value: 35 }, { day: 'Jeu', value: 28 }, { day: 'Ven', value: 41 }, { day: 'Sam', value: 12 }, { day: 'Dim', value: 9 }
  ]);
});

export default router; 
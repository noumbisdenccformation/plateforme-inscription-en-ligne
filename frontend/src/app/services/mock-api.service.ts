import { Injectable } from '@angular/core';
import { of } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class MockApiService {
  getDashboardStats() {
    return of({
      totalInscriptions: 1280,
      pending: 312,
      approved: 842,
      rejected: 126,
      completionByStep: [78, 64, 52, 39, 21]
    });
  }

  getHeatmap() {
    return of([
      { day: 'Lun', value: 14 }, { day: 'Mar', value: 22 }, { day: 'Mer', value: 35 }, { day: 'Jeu', value: 28 }, { day: 'Ven', value: 41 }, { day: 'Sam', value: 12 }, { day: 'Dim', value: 9 }
    ]);
  }

  getDossiers(page = 1, pageSize = 20) {
    const items = Array.from({ length: pageSize }).map((_, i) => {
      const id = (page - 1) * pageSize + i + 1;
      const states = ['PENDING', 'APPROVED', 'REJECTED'];
      return {
        id,
        candidat: `Candidat ${id}`,
        completude: Math.floor(Math.random() * 100),
        etat: states[Math.floor(Math.random() * states.length)],
        deposeLe: new Date(Date.now() - Math.random() * 1e10).toISOString()
      };
    });
    return of({ items, total: 200 });
  }
} 
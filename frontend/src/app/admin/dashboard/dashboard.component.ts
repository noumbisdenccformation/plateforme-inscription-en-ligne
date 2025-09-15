import { Component, OnInit } from '@angular/core';
import { MockApiService } from '../../services/mock-api.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  stats: any;
  heatmap: any[] = [];

  constructor(private api: MockApiService) {}

  ngOnInit(): void {
    this.api.getDashboardStats().subscribe(s => this.stats = s);
    this.api.getHeatmap().subscribe(h => this.heatmap = h);
  }
} 
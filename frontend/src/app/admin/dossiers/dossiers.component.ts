import { Component, OnInit } from '@angular/core';
import { MockApiService } from '../../services/mock-api.service';

@Component({
  selector: 'app-admin-dossiers',
  templateUrl: './dossiers.component.html',
  styleUrls: ['./dossiers.component.css']
})
export class DossiersComponent implements OnInit {
  items: any[] = [];
  total = 0;
  page = 1;
  pageSize = 20;

  constructor(private api: MockApiService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.api.getDossiers(this.page, this.pageSize).subscribe(res => {
      this.items = res.items;
      this.total = res.total;
    });
  }

  next(): void { if (this.page * this.pageSize < this.total) { this.page++; this.load(); } }
  prev(): void { if (this.page > 1) { this.page--; this.load(); } }
} 
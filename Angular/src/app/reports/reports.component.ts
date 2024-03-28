import { Component } from '@angular/core';
import { ReportDTO } from '../model/reportDTO';
import { ReportService } from '../service/report.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.css'],
})
export class ReportsComponent {
  public reposrts!: ReportDTO[];
  constructor(private reportService: ReportService, private router: Router) {}

  ngOnInit(): void {
    this.reportService.getAllReports().subscribe((response: ReportDTO[]) => {
      this.reposrts = response;
      console.log('Reposrts: ' + response);
    });
  }

  public inspect(postId: number) {
    this.router.navigate(['/posts', postId]);
  }
}

import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChartModule } from 'primeng/chart';
import { CardModule } from 'primeng/card';
import { DropdownModule } from 'primeng/dropdown';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { CalendarModule } from 'primeng/calendar';
import { ReporteService, ReporteProcesosEstado,
         ReporteProcesosNivel, GrupoConteo } from '../../services/reporte.service';
import * as XLSX from 'xlsx';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-reportes-procesos',
  standalone: true,
  imports: [CommonModule, FormsModule, ChartModule, CardModule,
            DropdownModule, ButtonModule, TableModule, CalendarModule],
  templateUrl: './reportes-procesos.component.html'
})
export class ReportesProcesosComponent {

  tipoReporte = 'estado';
  tiposReporte = [
    { label: 'Por Estado',           value: 'estado' },
    { label: 'Por Nivel Jerárquico', value: 'nivel'  },
    { label: 'Por Unidad',           value: 'unidad' }
  ];
  fechaDesde: Date | null = null;
  fechaHasta: Date | null = null;

  cargando  = false;
  generado  = false;
  errorMsg  = '';

  datosEstado: ReporteProcesosEstado | null = null;
  datosNivel:  ReporteProcesosNivel  | null = null;
  datosUnidad: GrupoConteo[]               = [];

  columnas: { header: string; field: string }[] = [];
  filas:    any[] = [];

  chartType: 'bar' | 'line' | 'pie' | 'doughnut' | 'polarArea' | 'radar' | 'scatter' | 'bubble' = 'doughnut';
  chartData: any = null;
  chartOptions = { responsive: true, plugins: { legend: { position: 'bottom' } } };

  constructor(private reporteService: ReporteService) {}

  private formatDate(d: Date): string {
    const y  = d.getFullYear();
    const m  = String(d.getMonth() + 1).padStart(2, '0');
    const dd = String(d.getDate()).padStart(2, '0');
    return `${y}-${m}-${dd}`;
  }
  private get desde(): string | undefined { return this.fechaDesde ? this.formatDate(this.fechaDesde) : undefined; }
  private get hasta(): string | undefined { return this.fechaHasta ? this.formatDate(this.fechaHasta) : undefined; }

  generarReporte(): void {
    this.cargando = true; this.generado = false; this.errorMsg = '';
    switch (this.tipoReporte) {
      case 'estado': this.cargarEstado(); break;
      case 'nivel':  this.cargarNivel();  break;
      case 'unidad': this.cargarUnidad(); break;
    }
  }

  private cargarEstado(): void {
    this.reporteService.procesosPorEstado(undefined, this.desde, this.hasta).subscribe({
      next: d => {
        this.datosEstado = d;
        this.columnas = [{ header: 'Estado', field: 'grupo' }, { header: 'Cantidad', field: 'cantidad' }, { header: 'Porcentaje', field: 'porcentaje' }];
        this.filas = d.porEstado.map(g => ({ grupo: g.grupo, cantidad: g.cantidad, porcentaje: g.porcentaje.toFixed(1) + '%' }));
        this.chartType = 'doughnut' as const;
        this.chartData = { labels: d.porEstado.map(g => g.grupo), datasets: [{ data: d.porEstado.map(g => g.cantidad), backgroundColor: ['#e6f1fb','#faeeda','#e1f5ee','#fcebeb','#f5c4b3','#9FE1CB','#AFA9EC','#C0DD97'] }] };
        this.cargando = false; this.generado = true;
      },
      error: () => { this.errorMsg = 'Error al cargar datos de estado.'; this.cargando = false; }
    });
  }

  private cargarNivel(): void {
    this.reporteService.procesosPorNivel(this.desde, this.hasta).subscribe({
      next: d => {
        this.datosNivel = d;
        this.columnas = [{ header: 'Nivel', field: 'nivel' }, { header: 'Total', field: 'total' }];
        this.filas = [
          { nivel: 'Macroprocesos',  total: d.totalMacroprocesos  },
          { nivel: 'Procesos N1',    total: d.totalProcesosN1     },
          { nivel: 'Procesos N2',    total: d.totalProcesosN2     },
          { nivel: 'Subprocesos N1', total: d.totalSubprocesosN1  },
          { nivel: 'Subprocesos N2', total: d.totalSubprocesosN2  },
        ];
        this.chartType = 'bar' as const;
        this.chartData = {
          labels: ['Macroprocesos','Procesos N1','Procesos N2','Subprocesos N1','Subprocesos N2'],
          datasets: [{ label: 'Total', data: [d.totalMacroprocesos, d.totalProcesosN1, d.totalProcesosN2, d.totalSubprocesosN1, d.totalSubprocesosN2], backgroundColor: ['#1D9E75','#378ADD','#7F77DD','#D85A30','#BA7517'] }]
        };
        this.cargando = false; this.generado = true;
      },
      error: () => { this.errorMsg = 'Error al cargar datos de nivel.'; this.cargando = false; }
    });
  }

  private cargarUnidad(): void {
    this.reporteService.procesosPorUnidad(this.desde, this.hasta).subscribe({
      next: d => {
        this.datosUnidad = d;
        this.columnas = [{ header: 'Unidad', field: 'grupo' }, { header: 'Procesos', field: 'cantidad' }, { header: 'Porcentaje', field: 'porcentaje' }];
        this.filas = d.map(g => ({ grupo: g.grupo, cantidad: g.cantidad, porcentaje: g.porcentaje.toFixed(1) + '%' }));
        this.chartType = 'bar' as const;
        this.chartData = { labels: d.map(g => g.grupo), datasets: [{ label: 'Procesos', data: d.map(g => g.cantidad), backgroundColor: ['#1a7b4e','#378ADD','#7F77DD','#D85A30','#BA7517','#1D9E75','#5DCAA5'] }] };
        this.cargando = false; this.generado = true;
      },
      error: () => { this.errorMsg = 'Error al cargar datos de unidad.'; this.cargando = false; }
    });
  }

  exportarXLSX(): void {
    if (!this.generado) return;
    const wb = XLSX.utils.book_new();
    const ws = XLSX.utils.json_to_sheet(this.filas.map(f => { const r: any = {}; this.columnas.forEach(c => r[c.header] = f[c.field]); return r; }));
    const tipo = this.tiposReporte.find(t => t.value === this.tipoReporte)?.label ?? 'Reporte';
    XLSX.utils.book_append_sheet(wb, ws, tipo);
    XLSX.writeFile(wb, `reporte_procesos_${this.tipoReporte}_SGI_ESPE.xlsx`);
  }

  exportarPDF(): void {
    if (!this.generado) return;
    const doc = new jsPDF();
    const tipo = this.tiposReporte.find(t => t.value === this.tipoReporte)?.label ?? 'Reporte';
    doc.setFontSize(16);
    doc.text(`Reporte de Procesos — ${tipo}`, 14, 20);
    if (this.fechaDesde && this.fechaHasta) { doc.setFontSize(10); doc.text(`Período: ${this.formatDate(this.fechaDesde)} — ${this.formatDate(this.fechaHasta)}`, 14, 28); }
    if (this.tipoReporte === 'estado' && this.datosEstado) { doc.setFontSize(10); doc.text(`Total: ${this.datosEstado.totalProcesos}  |  Avance promedio: ${this.datosEstado.promedioAvance}%`, 14, 34); }
    autoTable(doc, { head: [this.columnas.map(c => c.header)], body: this.filas.map(f => this.columnas.map(c => String(f[c.field] ?? ''))), startY: 40, headStyles: { fillColor: [26, 123, 78] } });
    doc.save(`reporte_procesos_${this.tipoReporte}_SGI_ESPE.pdf`);
  }
}

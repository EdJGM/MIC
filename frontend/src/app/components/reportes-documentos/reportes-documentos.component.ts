import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChartModule } from 'primeng/chart';
import { CardModule } from 'primeng/card';
import { DropdownModule } from 'primeng/dropdown';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { InputNumberModule } from 'primeng/inputnumber';
import { ReporteService, ReporteDocumentosEstado,
         ReporteDocumentosTipo, ReporteDocumentosUnidad,
         ReporteDocumentosFecha } from '../../services/reporte.service';
import * as XLSX from 'xlsx';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-reportes-documentos',
  standalone: true,
  imports: [CommonModule, FormsModule, ChartModule, CardModule,
            DropdownModule, ButtonModule, TableModule, InputNumberModule],
  templateUrl: './reportes-documentos.component.html'
})
export class ReportesDocumentosComponent {

  tipoReporte = 'estado';
  tiposReporte = [
    { label: 'Por Estado',  value: 'estado' },
    { label: 'Por Tipo',    value: 'tipo'   },
    { label: 'Por Unidad',  value: 'unidad' },
    { label: 'Por Fecha',   value: 'fecha'  },
  ];
  anioDesde = new Date().getFullYear() - 2;
  anioHasta = new Date().getFullYear();

  cargando = false;
  generado = false;
  errorMsg = '';

  datosEstado: ReporteDocumentosEstado | null = null;
  datosTipo:   ReporteDocumentosTipo   | null = null;
  datosUnidad: ReporteDocumentosUnidad | null = null;
  datosFecha:  ReporteDocumentosFecha  | null = null;

  columnas: { header: string; field: string }[] = [];
  filas:    any[] = [];

  chartType: 'bar' | 'line' | 'pie' | 'doughnut' | 'polarArea' | 'radar' | 'scatter' | 'bubble' = 'pie';
  chartData: any = null;
  chartOptions = { responsive: true, plugins: { legend: { position: 'bottom' } } };

  private readonly COLORS = ['#1a7b4e','#378ADD','#7F77DD','#D85A30','#BA7517','#1D9E75','#5DCAA5','#C0DD97'];

  constructor(private reporteService: ReporteService) {}

  generarReporte(): void {
    this.cargando = true; this.generado = false; this.errorMsg = '';
    switch (this.tipoReporte) {
      case 'estado': this.cargarEstado(); break;
      case 'tipo':   this.cargarTipo();   break;
      case 'unidad': this.cargarUnidad(); break;
      case 'fecha':  this.cargarFecha();  break;
    }
  }

  private cargarEstado(): void {
    this.reporteService.documentosPorEstado().subscribe({
      next: d => {
        this.datosEstado = d;
        this.columnas = [{ header: 'Estado', field: 'estado' }, { header: 'Cantidad', field: 'cantidad' }];
        this.filas = [{ estado: 'Activos', cantidad: d.activos }, { estado: 'Suspendidos', cantidad: d.suspendidos }, { estado: 'Obsoletos', cantidad: d.obsoletos }, { estado: 'Total', cantidad: d.total }];
        this.chartType = 'pie' as const;
        this.chartData = { labels: ['Activos','Suspendidos','Obsoletos'], datasets: [{ data: [d.activos, d.suspendidos, d.obsoletos], backgroundColor: ['#e1f5ee','#faeeda','#fcebeb'] }] };
        this.cargando = false; this.generado = true;
      },
      error: () => { this.errorMsg = 'Error al cargar datos de estado.'; this.cargando = false; }
    });
  }

  private cargarTipo(): void {
    this.reporteService.documentosPorTipo().subscribe({
      next: d => {
        this.datosTipo = d;
        this.columnas = [{ header: 'Tipo', field: 'tipo' }, { header: 'Cantidad', field: 'cantidad' }, { header: 'Porcentaje', field: 'porcentaje' }];
        this.filas = d.porTipo.map(g => ({ tipo: g.grupo, cantidad: g.cantidad, porcentaje: g.porcentaje.toFixed(1) + '%' }));
        this.chartType = 'pie' as const;
        this.chartData = { labels: d.porTipo.map(g => g.grupo), datasets: [{ data: d.porTipo.map(g => g.cantidad), backgroundColor: this.COLORS }] };
        this.cargando = false; this.generado = true;
      },
      error: () => { this.errorMsg = 'Error al cargar datos por tipo.'; this.cargando = false; }
    });
  }

  private cargarUnidad(): void {
    this.reporteService.documentosPorUnidad().subscribe({
      next: d => {
        this.datosUnidad = d;
        this.columnas = [{ header: 'Unidad', field: 'unidad' }, { header: 'Cantidad', field: 'cantidad' }, { header: 'Porcentaje', field: 'porcentaje' }];
        this.filas = d.porUnidad.map(g => ({ unidad: g.grupo, cantidad: g.cantidad, porcentaje: g.porcentaje.toFixed(1) + '%' }));
        this.chartType = 'bar' as const;
        this.chartData = { labels: d.porUnidad.map(g => g.grupo), datasets: [{ label: 'Documentos', data: d.porUnidad.map(g => g.cantidad), backgroundColor: this.COLORS }] };
        this.cargando = false; this.generado = true;
      },
      error: () => { this.errorMsg = 'Error al cargar datos por unidad.'; this.cargando = false; }
    });
  }

  private cargarFecha(): void {
    this.reporteService.documentosPorFecha(this.anioDesde, this.anioHasta).subscribe({
      next: d => {
        this.datosFecha = d;
        this.columnas = [{ header: 'Mes/Año', field: 'mes' }, { header: 'Cantidad', field: 'cantidad' }];
        this.filas = d.porMes.map(g => ({ mes: g.grupo, cantidad: g.cantidad }));
        this.chartType = 'line' as const;
        this.chartData = { labels: d.porMes.map(g => g.grupo), datasets: [{ label: 'Documentos', data: d.porMes.map(g => g.cantidad), borderColor: '#1a7b4e', backgroundColor: '#e1f5ee', fill: true }] };
        this.cargando = false; this.generado = true;
      },
      error: () => { this.errorMsg = 'Error al cargar datos por fecha.'; this.cargando = false; }
    });
  }

  exportarXLSX(): void {
    if (!this.generado) return;
    const wb = XLSX.utils.book_new();
    const ws = XLSX.utils.json_to_sheet(this.filas.map(f => { const r: any = {}; this.columnas.forEach(c => r[c.header] = f[c.field]); return r; }));
    const tipo = this.tiposReporte.find(t => t.value === this.tipoReporte)?.label ?? 'Reporte';
    XLSX.utils.book_append_sheet(wb, ws, tipo);
    XLSX.writeFile(wb, `reporte_documentos_${this.tipoReporte}_SGI_ESPE.xlsx`);
  }

  exportarPDF(): void {
    if (!this.generado) return;
    const doc = new jsPDF();
    const tipo = this.tiposReporte.find(t => t.value === this.tipoReporte)?.label ?? 'Reporte';
    doc.setFontSize(16);
    doc.text(`Reporte de Documentos — ${tipo}`, 14, 20);
    if (this.tipoReporte === 'fecha') { doc.setFontSize(10); doc.text(`Período: ${this.anioDesde} — ${this.anioHasta}`, 14, 28); }
    if (this.tipoReporte === 'estado' && this.datosEstado) { doc.setFontSize(10); doc.text(`Total: ${this.datosEstado.total} documentos`, 14, 28); }
    autoTable(doc, { head: [this.columnas.map(c => c.header)], body: this.filas.map(f => this.columnas.map(c => String(f[c.field] ?? ''))), startY: 32, headStyles: { fillColor: [26, 123, 78] } });
    doc.save(`reporte_documentos_${this.tipoReporte}_SGI_ESPE.pdf`);
  }
}

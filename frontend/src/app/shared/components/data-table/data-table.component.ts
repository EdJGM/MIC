import { Component, Input, ContentChild, TemplateRef, ChangeDetectionStrategy, ChangeDetectorRef, AfterContentInit, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { COLORS } from '../../constants/colors.constant';

@Component({
  selector: 'app-data-table',
  standalone: true,
  imports: [CommonModule, TableModule],
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <div class="table-wrapper-custom">
      <p-table
        [value]="value"
        [columns]="columns"
        [paginator]="showPaginator"
        [rows]="rows"
        [rowsPerPageOptions]="rowsPerPageOptions"
        [globalFilterFields]="globalFilterFields"
        [sortField]="sortField"
        [sortOrder]="sortOrder"
        [sortMode]="sortMode"
        [styleClass]="tableClass"
        [scrollable]="scrollable"
        [scrollHeight]="scrollHeight"
        [rowHover]="true"
        [lazy]="lazy"
        [loading]="loading"
        [totalRecords]="totalRecords"
        paginatorPosition="top"
        currentPageReportTemplate="Mostrando {first} a {last} de {totalRecords} registros"
        [showCurrentPageReport]="true"
        (onLazyLoad)="onLazyLoad($event)"
        (onSort)="onSort($event)">

        <!-- Header Template: usa el proyectado (#header) o el interno por columnas -->
        <ng-template pTemplate="header" let-columns>
          <ng-container *ngIf="headerTemplate; else defaultHeader">
            <ng-container *ngTemplateOutlet="headerTemplate"></ng-container>
          </ng-container>
          <ng-template #defaultHeader>
            <tr>
              <th *ngFor="let col of columns" [pSortableColumn]="col.field" class="text-center font-bold py-4">
                <div class="flex items-center justify-center gap-2">
                  {{ col.header }}
                  <p-sortIcon [field]="col.field"></p-sortIcon>
                </div>
              </th>
              <th *ngIf="showActions" class="text-center font-bold py-4 min-w-[150px]">
                <div class="flex items-center justify-center gap-2">
                  <i class="pi pi-cog"></i>
                  {{ actionsLabel }}
                </div>
              </th>
            </tr>
          </ng-template>
        </ng-template>

        <!-- Body template projection -->
        <ng-template pTemplate="body" let-rowData let-rowIndex="rowIndex">
          <ng-container *ngTemplateOutlet="bodyTemplate; context: { $implicit: rowData, rowIndex: rowIndex }"></ng-container>
        </ng-template>

        <!-- Empty message template -->
        <ng-template pTemplate="emptymessage">
          <tr>
            <td [attr.colspan]="colSpan" class="text-center p-8">
              <div class="flex flex-col items-center justify-center py-10">
                <i class="pi pi-inbox text-5xl text-gray-400 mb-4"></i>
                <p class="font-semibold text-gray-700 text-lg">{{ emptyMessageTitle }}</p>
                <p class="text-sm text-gray-500 mt-1">{{ emptyMessageSubtitle }}</p>
              </div>
            </td>
          </tr>
        </ng-template>

        <!-- Loading template -->
        <ng-template pTemplate="loadingbody" *ngIf="loading">
          <tr>
            <td [attr.colspan]="colSpan" class="text-center p-8">
              <div class="flex flex-col items-center justify-center py-10">
                <i class="pi pi-spin pi-spinner text-4xl text-indigo-500 mb-4"></i>
                <p class="text-gray-600">Cargando datos...</p>
              </div>
            </td>
          </tr>
        </ng-template>
      </p-table>
    </div>
  `,
  styles: [`
    .table-wrapper-custom {
      width: 100%;
      overflow-x: auto;
    }

    /* Usar !important para sobrescribir cualquier estilo del padre */
    :host ::ng-deep .p-datatable .p-datatable-thead > tr > th {
      background: linear-gradient(to right, ${COLORS.PRIMARY}, ${COLORS.PRIMARY_HOVER}) !important;
      color: white !important;
      font-size: 0.875rem !important;
      font-weight: bold !important;
      border-right: 1px solid rgba(255, 255, 255, 0.1) !important;
      white-space: nowrap !important;
      position: relative !important;
      padding: 1rem 0.75rem !important;
      text-align: center !important;
    }

    :host ::ng-deep .p-datatable .p-datatable-thead > tr > th:hover {
      background: linear-gradient(to right, ${COLORS.PRIMARY_HOVER}, ${COLORS.PRIMARY}) !important;
    }

    :host ::ng-deep .p-datatable .p-datatable-thead > tr > th .p-sortable-column-icon {
      color: rgba(255, 255, 255, 0.8) !important;
      margin-left: 0.5rem !important;
    }

    :host ::ng-deep .p-datatable .p-datatable-thead > tr > th.p-highlight {
      background: linear-gradient(to right, ${COLORS.PRIMARY_HOVER}, ${COLORS.PRIMARY}) !important;
      color: white !important;
    }

    :host ::ng-deep .p-datatable .p-datatable-thead > tr > th.p-highlight .p-sortable-column-icon {
      color: #ffd700 !important;
    }

    :host ::ng-deep .p-datatable .p-datatable-tbody > tr > td {
      font-size: 0.875rem !important;
      border: 1px solid #e0e0e0 !important;
      padding: 1rem 0.75rem !important;
      vertical-align: middle !important;
    }

    :host ::ng-deep .p-datatable .p-datatable-tbody > tr:hover {
      background-color: ${COLORS.PRIMARY_LIGHTER} !important;
      transform: translateY(-1px);
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      transition: all 0.2s ease;
    }

    :host ::ng-deep .p-datatable {
      border: 1px solid ${COLORS.PRIMARY_BORDER} !important;
      border-radius: 8px !important;
      overflow: hidden !important;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08) !important;
    }

    :host ::ng-deep .p-paginator {
      background: ${COLORS.GRAY_LIGHTER} !important;
      border: none !important;
      padding: 0.75rem !important;
      border-top: 1px solid #e0e0e0 !important;
    }

    :host ::ng-deep .p-paginator .p-paginator-pages .p-paginator-page.p-highlight {
      background: ${COLORS.PRIMARY} !important;
      color: white !important;
    }

    /* Responsive */
    @media (max-width: 768px) {
      :host ::ng-deep .p-datatable .p-datatable-thead > tr > th,
      :host ::ng-deep .p-datatable .p-datatable-tbody > tr > td {
        padding: 0.75rem 0.5rem !important;
        font-size: 0.825rem !important;
      }
    }
  `]
})
export class DataTableComponent implements AfterContentInit, OnChanges {
  // Propiedades básicas de la tabla
  @Input() value: any[] = [];
  @Input() columns: any[] = [];
  @Input() colSpan: number = 1;

  // Acciones
  @Input() showActions: boolean = false;
  @Input() actionsLabel: string = 'Acciones';

  // Configuración de paginación
  @Input() showPaginator: boolean = true;
  @Input() rows: number = 10;
  @Input() rowsPerPageOptions: number[] = [5, 10, 20, 50];

  // Configuración de sorting
  @Input() sortField: string = '';
  @Input() sortOrder: number = 1;
  @Input() sortMode: 'single' | 'multiple' = 'single';

  // Configuración de filtros
  @Input() globalFilterFields: string[] = [];

  // Configuración de scroll
  @Input() scrollable: boolean = false;
  @Input() scrollHeight: string = '';

  // Configuración de lazy loading
  @Input() lazy: boolean = false;
  @Input() loading: boolean = false;
  @Input() totalRecords: number = 0;

  // Mensajes personalizados
  @Input() emptyMessageTitle: string = 'No hay datos';
  @Input() emptyMessageSubtitle: string = 'No se encontraron registros para mostrar';

  // Estilos
  @Input() styleClass: string = '';

  // Templates proyectados
  @ContentChild('header') headerTemplate!: TemplateRef<any>;
  @ContentChild('body') bodyTemplate!: TemplateRef<any>;

  constructor(private cdr: ChangeDetectorRef) { }

  get tableClass(): string {
    return `p-datatable-striped p-datatable-gridlines ${this.styleClass}`;
  }

  ngAfterContentInit(): void {
    // Detectar cambios después de que el contenido esté listo
    setTimeout(() => {
      this.cdr.detectChanges();
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    // Detectar cambios cuando las propiedades cambien
    if (changes['value'] || changes['columns']) {
      this.cdr.detectChanges();
    }
  }

  onLazyLoad(event: any): void {
    console.log('Lazy load event:', event);
  }

  onSort(event: any): void {
    console.log('Sort event:', event);
  }
}
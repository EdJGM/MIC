import { Component, Input, ContentChild, TemplateRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PanelModule } from 'primeng/panel';
import { COLORS } from '../../constants/colors.constant';

@Component({
  selector: 'app-panel',
  standalone: true,
  imports: [CommonModule, PanelModule],
  template: `
    <p-panel
      [header]="header"
      [toggleable]="toggleable"
      [collapsed]="collapsed"
      [styleClass]="panelClass"
      [style]="panelStyle">
      <ng-template pTemplate="icons" *ngIf="iconsTemplate">
        <ng-container *ngTemplateOutlet="iconsTemplate"></ng-container>
      </ng-template>
      <ng-template pTemplate="header" *ngIf="headerTemplate">
        <ng-container *ngTemplateOutlet="headerTemplate"></ng-container>
      </ng-template>
      <ng-content></ng-content>
    </p-panel>
  `,
  styles: [`
    :host ::ng-deep .p-panel .p-panel-header {
      background: linear-gradient(135deg, ${COLORS.PRIMARY} 0%, ${COLORS.PRIMARY_DARK} 100%);
      color: white;
      border-radius: 0.75rem 0.75rem 0 0;
      padding: 1rem 1.5rem;
      font-size: 1.1rem;
      font-weight: 600;
    }

    :host ::ng-deep .p-panel .p-panel-content {
      padding: 1.5rem;
      background-color: ${COLORS.GRAY_LIGHTER};
    }

    :host ::ng-deep .p-panel {
      border: 1px solid ${COLORS.PRIMARY_BORDER};
      border-radius: 0.75rem;
      box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
      overflow: hidden;
    }

    :host ::ng-deep .p-panel .p-panel-icons {
      display: flex;
      align-items: center;
      gap: 0.5rem;
    }
  `]
})
export class PanelComponent {
  @Input() header: string = '';
  @Input() toggleable: boolean = false;
  @Input() collapsed: boolean = false;
  @Input() styleClass: string = '';
  @Input() customStyle: { [key: string]: string } = {};

  @ContentChild('icons') iconsTemplate!: TemplateRef<any>;
  @ContentChild('headerContent') headerTemplate!: TemplateRef<any>;

  get panelClass(): string {
    return this.styleClass;
  }

  get panelStyle(): { [key: string]: string } {
    return {
      'background-color': COLORS.WHITE,
      ...this.customStyle
    };
  }
}

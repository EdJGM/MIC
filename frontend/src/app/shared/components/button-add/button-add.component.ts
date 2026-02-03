import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { TooltipModule } from 'primeng/tooltip';
import { COLORS } from '../../constants/colors.constant';

@Component({
  selector: 'app-button-add',
  standalone: true,
  imports: [CommonModule, ButtonModule, RippleModule, TooltipModule],
  template: `
    <button
      pButton
      pRipple
      [type]="type"
      [label]="showLabel ? label : ''"
      [icon]="icon"
      [disabled]="disabled"
      [loading]="loading"
      [pTooltip]="tooltip"
      [tooltipPosition]="tooltipPosition"
      [class]="buttonClass"
      [ngStyle]="buttonStyle"
      (click)="onClick.emit($event)">
    </button>
  `
})
export class ButtonAddComponent {
  @Input() type: 'button' | 'submit' | 'reset' = 'button';
  @Input() label: string = 'Nuevo';
  @Input() showLabel: boolean = false;
  @Input() icon: string = 'pi pi-plus';
  @Input() disabled: boolean = false;
  @Input() loading: boolean = false;
  @Input() tooltip: string = 'Agregar nuevo';
  @Input() tooltipPosition: 'top' | 'bottom' | 'left' | 'right' = 'top';
  @Input() rounded: boolean = true;
  @Input() size: 'small' | 'normal' | 'large' = 'normal';
  @Input() styleClass: string = '';
  @Input() customStyle: { [key: string]: string } = {};

  @Output() onClick = new EventEmitter<Event>();

  get buttonClass(): string {
    const roundedClass = this.rounded ? 'p-button-rounded' : '';
    const sizeClass = this.size === 'small' ? 'p-button-sm' : this.size === 'large' ? 'p-button-lg' : '';
    return `font-semibold shadow-md hover:shadow-lg transition-all duration-200 ${roundedClass} ${sizeClass} ${this.styleClass}`;
  }

  get buttonStyle(): { [key: string]: string } {
    const sizeStyles: { [key: string]: { width: string; height: string } } = {
      'small': { width: '2.5rem', height: '2.5rem' },
      'normal': { width: '3rem', height: '3rem' },
      'large': { width: '3.5rem', height: '3.5rem' }
    };

    return {
      'background-color': COLORS.BLUE,
      'border-color': COLORS.BLUE,
      'color': COLORS.WHITE,
      ...(this.rounded && !this.showLabel ? sizeStyles[this.size] : {}),
      ...this.customStyle
    };
  }
}

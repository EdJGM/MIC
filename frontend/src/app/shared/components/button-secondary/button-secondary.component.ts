import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { TooltipModule } from 'primeng/tooltip';
import { COLORS } from '../../constants/colors.constant';

@Component({
  selector: 'app-button-secondary',
  standalone: true,
  imports: [CommonModule, ButtonModule, RippleModule, TooltipModule],
  template: `
    <button
      pButton
      pRipple
      [type]="type"
      [label]="label"
      [icon]="icon"
      [iconPos]="iconPos"
      [disabled]="disabled"
      [loading]="loading"
      [pTooltip]="tooltip"
      [tooltipPosition]="tooltipPosition"
      [class]="buttonClass"
      [ngStyle]="buttonStyle"
      (click)="onClick.emit($event)">
      <ng-content></ng-content>
    </button>
  `
})
export class ButtonSecondaryComponent {
  @Input() type: 'button' | 'submit' | 'reset' = 'button';
  @Input() label: string = '';
  @Input() icon: string = '';
  @Input() iconPos: 'left' | 'right' | 'top' | 'bottom' = 'left';
  @Input() disabled: boolean = false;
  @Input() loading: boolean = false;
  @Input() tooltip: string = '';
  @Input() tooltipPosition: 'top' | 'bottom' | 'left' | 'right' = 'top';
  @Input() styleClass: string = '';
  @Input() customStyle: { [key: string]: string } = {};
  @Input() fullWidth: boolean = false;
  @Input() size: 'small' | 'normal' | 'large' = 'normal';

  @Output() onClick = new EventEmitter<Event>();

  get buttonClass(): string {
    const widthClass = this.fullWidth ? 'w-full' : '';
    const sizeClass = this.size === 'small' ? 'p-button-sm' : this.size === 'large' ? 'p-button-lg' : '';
    return `font-semibold shadow-md hover:shadow-lg transition-all duration-200 ${widthClass} ${sizeClass} ${this.styleClass}`;
  }

  get buttonStyle(): { [key: string]: string } {
    const heights: { [key: string]: string } = {
      'small': '2.5rem',
      'normal': '3rem',
      'large': '3.5rem'
    };
    return {
      'height': heights[this.size],
      'background-color': COLORS.SECONDARY,
      'border-color': COLORS.SECONDARY,
      'color': COLORS.WHITE,
      'padding': '0.75rem 1.5rem',
      'border-radius': '0.5rem',
      ...this.customStyle
    };
  }
}

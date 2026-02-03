import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { COLORS } from '../../constants/colors.constant';

@Component({
  selector: 'app-loading-spinner',
  standalone: true,
  imports: [CommonModule, ProgressSpinnerModule],
  template: `
    <div [class]="containerClass" [ngStyle]="containerStyle">
      <p-progressSpinner
        [style]="spinnerStyle"
        [strokeWidth]="strokeWidth"
        fill="transparent"
        [animationDuration]="animationDuration">
      </p-progressSpinner>
      <p *ngIf="message" class="mt-3 text-gray-600 font-medium">{{ message }}</p>
    </div>
  `,
  styles: [`
    :host ::ng-deep .p-progress-spinner-circle {
      stroke: ${COLORS.PRIMARY};
    }
  `]
})
export class LoadingSpinnerComponent {
  @Input() size: 'small' | 'medium' | 'large' = 'medium';
  @Input() strokeWidth: string = '4';
  @Input() animationDuration: string = '1s';
  @Input() message: string = '';
  @Input() overlay: boolean = false;
  @Input() fullScreen: boolean = false;
  @Input() styleClass: string = '';

  get containerClass(): string {
    const baseClass = 'flex flex-col items-center justify-center';
    const overlayClass = this.overlay ? 'absolute inset-0 bg-white bg-opacity-80 z-50' : '';
    const fullScreenClass = this.fullScreen ? 'fixed inset-0 bg-white bg-opacity-90 z-50' : '';
    return `${baseClass} ${overlayClass} ${fullScreenClass} ${this.styleClass}`;
  }

  get containerStyle(): { [key: string]: string } {
    if (this.overlay || this.fullScreen) {
      return {};
    }
    return { 'padding': '2rem' };
  }

  get spinnerStyle(): { [key: string]: string } {
    const sizes: { [key: string]: { width: string; height: string } } = {
      'small': { width: '40px', height: '40px' },
      'medium': { width: '60px', height: '60px' },
      'large': { width: '80px', height: '80px' }
    };
    return sizes[this.size];
  }
}

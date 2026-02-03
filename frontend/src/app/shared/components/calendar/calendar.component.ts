import { Component, Input, Output, EventEmitter, forwardRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { CalendarModule } from 'primeng/calendar';
import { COLORS } from '../../constants/colors.constant';

@Component({
  selector: 'app-calendar',
  standalone: true,
  imports: [CommonModule, FormsModule, CalendarModule],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => CalendarComponent),
      multi: true
    }
  ],
  template: `
    <p-calendar
      [inputId]="inputId"
      [name]="name"
      [placeholder]="placeholder"
      [disabled]="disabled"
      [readonlyInput]="readonlyInput"
      [showIcon]="showIcon"
      [showButtonBar]="showButtonBar"
      [dateFormat]="dateFormat"
      [selectionMode]="selectionMode"
      [showTime]="showTime"
      [hourFormat]="hourFormat"
      [minDate]="minDate"
      [maxDate]="maxDate"
      [styleClass]="calendarClass"
      [(ngModel)]="value"
      (ngModelChange)="onValueChange($event)"
      (onSelect)="onDateSelect.emit($event)"
      (onBlur)="onTouched()">
    </p-calendar>
  `,
  styles: [`
    :host ::ng-deep .p-calendar {
      width: 100%;
    }

    :host ::ng-deep .p-calendar .p-inputtext {
      padding: 0.75rem;
      background-color: white;
      border: 2px solid ${COLORS.PRIMARY};
      border-radius: 0.5rem;
    }

    :host ::ng-deep .p-datepicker {
      border: 2px solid ${COLORS.PRIMARY};
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }

    :host ::ng-deep .p-datepicker .p-datepicker-header {
      background: ${COLORS.PRIMARY_DARK};
      color: white;
      padding: 0.5rem;
    }

    :host ::ng-deep .p-datepicker .p-datepicker-header .p-datepicker-title button {
      color: white;
    }

    :host ::ng-deep .p-datepicker table td > span.p-highlight {
      background: ${COLORS.PRIMARY};
      color: white;
    }

    :host ::ng-deep .p-datepicker table td > span:focus {
      box-shadow: 0 0 0 0.2rem rgba(26, 123, 78, 0.25);
    }
  `]
})
export class CalendarComponent implements ControlValueAccessor {
  @Input() inputId: string = '';
  @Input() name: string = '';
  @Input() placeholder: string = 'Seleccione fecha';
  @Input() disabled: boolean = false;
  @Input() readonlyInput: boolean = false;
  @Input() showIcon: boolean = true;
  @Input() showButtonBar: boolean = true;
  @Input() dateFormat: string = 'dd/mm/yy';
  @Input() selectionMode: 'single' | 'multiple' | 'range' = 'single';
  @Input() showTime: boolean = false;
  @Input() hourFormat: '12' | '24' = '24';
  @Input() minDate: Date | null = null;
  @Input() maxDate: Date | null = null;
  @Input() styleClass: string = '';

  @Output() onDateSelect = new EventEmitter<any>();

  value: Date | Date[] | null = null;
  onChange: (value: any) => void = () => {};
  onTouched: () => void = () => {};

  get calendarClass(): string {
    return `w-full ${this.styleClass}`;
  }

  writeValue(value: any): void {
    this.value = value;
  }

  registerOnChange(fn: (value: any) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  onValueChange(value: any): void {
    this.value = value;
    this.onChange(value);
  }
}

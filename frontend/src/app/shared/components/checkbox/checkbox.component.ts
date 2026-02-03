import { Component, Input, Output, EventEmitter, forwardRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { CheckboxModule } from 'primeng/checkbox';
import { COLORS } from '../../constants/colors.constant';

@Component({
  selector: 'app-checkbox',
  standalone: true,
  imports: [CommonModule, FormsModule, CheckboxModule],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => CheckboxComponent),
      multi: true
    }
  ],
  template: `
    <div class="flex items-center gap-2">
      <p-checkbox
        [inputId]="inputId"
        [name]="name"
        [disabled]="disabled"
        [binary]="binary"
        [trueValue]="trueValue"
        [falseValue]="falseValue"
        [(ngModel)]="value"
        (ngModelChange)="onValueChange($event)"
        [styleClass]="checkboxClass">
      </p-checkbox>
      <label *ngIf="label" [for]="inputId" class="cursor-pointer select-none">
        {{ label }}
      </label>
    </div>
  `,
  styles: [`
    :host ::ng-deep .p-checkbox .p-checkbox-box {
      border: 2px solid ${COLORS.PRIMARY};
      border-radius: 0.375rem;
    }

    :host ::ng-deep .p-checkbox .p-checkbox-box.p-highlight {
      background: ${COLORS.PRIMARY};
      border-color: ${COLORS.PRIMARY};
    }

    :host ::ng-deep .p-checkbox .p-checkbox-box:hover {
      border-color: ${COLORS.PRIMARY_DARK};
    }
  `]
})
export class CheckboxComponent implements ControlValueAccessor {
  @Input() inputId: string = '';
  @Input() name: string = '';
  @Input() label: string = '';
  @Input() disabled: boolean = false;
  @Input() binary: boolean = true;
  @Input() trueValue: any = true;
  @Input() falseValue: any = false;
  @Input() styleClass: string = '';

  @Output() onCheck = new EventEmitter<any>();

  value: any = false;
  onChange: (value: any) => void = () => {};
  onTouched: () => void = () => {};

  get checkboxClass(): string {
    return this.styleClass;
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
    this.onCheck.emit(value);
  }
}

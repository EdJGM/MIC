import { Component, Input, Output, EventEmitter, forwardRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { COLORS } from '../../constants/colors.constant';

@Component({
  selector: 'app-input-text',
  standalone: true,
  imports: [CommonModule, FormsModule, InputTextModule],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => InputTextComponent),
      multi: true
    }
  ],
  template: `
    <input
      pInputText
      [type]="type"
      [id]="id"
      [name]="name"
      [placeholder]="placeholder"
      [disabled]="disabled"
      [readonly]="readonly"
      [required]="required"
      [class]="inputClass"
      [ngStyle]="inputStyle"
      [(ngModel)]="value"
      (ngModelChange)="onValueChange($event)"
      (blur)="onTouched()"
      (keyup.enter)="onEnter.emit($event)" />
  `
})
export class InputTextComponent implements ControlValueAccessor {
  @Input() type: 'text' | 'email' | 'password' | 'number' | 'tel' = 'text';
  @Input() id: string = '';
  @Input() name: string = '';
  @Input() placeholder: string = '';
  @Input() disabled: boolean = false;
  @Input() readonly: boolean = false;
  @Input() required: boolean = false;
  @Input() styleClass: string = '';
  @Input() customStyle: { [key: string]: string } = {};
  @Input() fullWidth: boolean = true;

  @Output() onEnter = new EventEmitter<Event>();

  value: string = '';
  onChange: (value: string) => void = () => {};
  onTouched: () => void = () => {};

  get inputClass(): string {
    const widthClass = this.fullWidth ? 'w-full' : '';
    return `bg-white border-2 rounded-lg focus:ring-2 focus:ring-opacity-50 transition-all duration-200 ${widthClass} ${this.styleClass}`;
  }

  get inputStyle(): { [key: string]: string } {
    return {
      'border-color': COLORS.PRIMARY_LIGHT,
      'padding': '0.75rem 1rem',
      ...this.customStyle
    };
  }

  writeValue(value: string): void {
    this.value = value || '';
  }

  registerOnChange(fn: (value: string) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  onValueChange(value: string): void {
    this.value = value;
    this.onChange(value);
  }
}

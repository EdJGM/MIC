import { Component, Input, Output, EventEmitter, forwardRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { COLORS } from '../../constants/colors.constant';

@Component({
  selector: 'app-textarea',
  standalone: true,
  imports: [CommonModule, FormsModule, InputTextareaModule],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => TextareaComponent),
      multi: true
    }
  ],
  template: `
    <textarea
      pInputTextarea
      [id]="id"
      [name]="name"
      [placeholder]="placeholder"
      [disabled]="disabled"
      [readonly]="readonly"
      [rows]="rows"
      [cols]="cols"
      [autoResize]="autoResize"
      [class]="textareaClass"
      [ngStyle]="textareaStyle"
      [(ngModel)]="value"
      (ngModelChange)="onValueChange($event)"
      (blur)="onTouched()">
    </textarea>
  `
})
export class TextareaComponent implements ControlValueAccessor {
  @Input() id: string = '';
  @Input() name: string = '';
  @Input() placeholder: string = '';
  @Input() disabled: boolean = false;
  @Input() readonly: boolean = false;
  @Input() rows: number = 3;
  @Input() cols: number = 30;
  @Input() autoResize: boolean = false;
  @Input() styleClass: string = '';
  @Input() customStyle: { [key: string]: string } = {};
  @Input() fullWidth: boolean = true;

  value: string = '';
  onChange: (value: string) => void = () => {};
  onTouched: () => void = () => {};

  get textareaClass(): string {
    const widthClass = this.fullWidth ? 'w-full' : '';
    return `bg-white border-2 rounded-lg focus:ring-2 focus:ring-opacity-50 transition-all duration-200 ${widthClass} ${this.styleClass}`;
  }

  get textareaStyle(): { [key: string]: string } {
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

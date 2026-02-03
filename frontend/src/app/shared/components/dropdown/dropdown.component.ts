import { Component, Input, Output, EventEmitter, forwardRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { COLORS } from '../../constants/colors.constant';

@Component({
  selector: 'app-dropdown',
  standalone: true,
  imports: [CommonModule, FormsModule, DropdownModule],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => DropdownComponent),
      multi: true
    }
  ],
  template: `
    <p-dropdown
      [options]="options"
      [optionLabel]="optionLabel"
      [optionValue]="optionValue"
      [placeholder]="placeholder"
      [disabled]="disabled"
      [filter]="filter"
      [filterBy]="filterBy"
      [showClear]="showClear"
      [editable]="editable"
      [styleClass]="dropdownClass"
      [(ngModel)]="value"
      (ngModelChange)="onValueChange($event)"
      (onBlur)="onTouched()"
      (onChange)="onSelectionChange.emit($event)"
      [style]="dropdownStyle">
    </p-dropdown>
  `
})
export class DropdownComponent implements ControlValueAccessor {
  @Input() options: any[] = [];
  @Input() optionLabel: string = 'label';
  @Input() optionValue: string = 'value';
  @Input() placeholder: string = 'Seleccione...';
  @Input() disabled: boolean = false;
  @Input() filter: boolean = false;
  @Input() filterBy: string = '';
  @Input() showClear: boolean = false;
  @Input() editable: boolean = false;
  @Input() styleClass: string = '';
  @Input() customStyle: { [key: string]: string } = {};
  @Input() fullWidth: boolean = true;

  @Output() onSelectionChange = new EventEmitter<any>();

  value: any = null;
  onChange: (value: any) => void = () => {};
  onTouched: () => void = () => {};

  get dropdownClass(): string {
    const widthClass = this.fullWidth ? 'w-full' : '';
    return `rounded-lg transition-all duration-200 ${widthClass} ${this.styleClass}`;
  }

  get dropdownStyle(): { [key: string]: string } {
    return {
      'border-color': COLORS.PRIMARY_LIGHT,
      'border-width': '2px',
      ...this.customStyle
    };
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

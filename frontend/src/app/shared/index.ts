/**
 * Shared Components - Exports centralizados
 * Importar desde '@shared' o './shared'
 */

// Constants
export { COLORS } from './constants/colors.constant';

// Button Components
import { ButtonPrimaryComponent } from './components/button-primary/button-primary.component';
import { ButtonSecondaryComponent } from './components/button-secondary/button-secondary.component';
import { ButtonCancelComponent } from './components/button-cancel/button-cancel.component';
import { ButtonAddComponent } from './components/button-add/button-add.component';

// Form Components
import { InputTextComponent } from './components/input-text/input-text.component';
import { TextareaComponent } from './components/textarea/textarea.component';
import { DropdownComponent } from './components/dropdown/dropdown.component';
import { CheckboxComponent } from './components/checkbox/checkbox.component';
import { CalendarComponent } from './components/calendar/calendar.component';

// Layout Components
import { PanelComponent } from './components/panel/panel.component';
import { DataTableComponent } from './components/data-table/data-table.component';

// Feedback Components
import { LoadingSpinnerComponent } from './components/loading-spinner/loading-spinner.component';

// Re-export all components
export {
  // Buttons
  ButtonPrimaryComponent,
  ButtonSecondaryComponent,
  ButtonCancelComponent,
  ButtonAddComponent,
  // Forms
  InputTextComponent,
  TextareaComponent,
  DropdownComponent,
  CheckboxComponent,
  CalendarComponent,
  // Layout
  PanelComponent,
  DataTableComponent,
  // Feedback
  LoadingSpinnerComponent,
};

/**
 * Array con todos los componentes para importar facilmente
 * Uso: imports: [...SHARED_COMPONENTS]
 */
export const SHARED_COMPONENTS = [
  // Buttons
  ButtonPrimaryComponent,
  ButtonSecondaryComponent,
  ButtonCancelComponent,
  ButtonAddComponent,
  // Forms
  InputTextComponent,
  TextareaComponent,
  DropdownComponent,
  CheckboxComponent,
  CalendarComponent,
  // Layout
  PanelComponent,
  DataTableComponent,
  // Feedback
  LoadingSpinnerComponent,
] as const;

/**
 * Constantes de colores institucionales ESPE
 * Centralizados para uso en componentes wrapper
 */
export const COLORS = {
  // Colores primarios ESPE
  PRIMARY: '#1a7b4e',
  PRIMARY_DARK: '#0d5c3a',
  PRIMARY_LIGHT: '#2b9a68',
  PRIMARY_LIGHTER: '#e8f5f0',
  PRIMARY_BORDER: '#2b9a68',
  PRIMARY_HOVER: '#0d5c3a',

  // Colores secundarios
  SECONDARY: '#6c757d',
  SECONDARY_DARK: '#343a40',
  SECONDARY_HOVER: '#5a6268',

  // Colores de estado
  SUCCESS: '#1a7b4e',
  SUCCESS_HOVER: '#0d5c3a',

  WARNING: '#ffc107',
  WARNING_HOVER: '#e0a800',
  WARNING_TEXT: '#212529',

  DANGER: '#dc3545',
  DANGER_HOVER: '#c82333',

  INFO: '#0077be',
  INFO_HOVER: '#005a8c',

  // Colores neutros
  WHITE: '#ffffff',
  BLACK: '#000000',
  GRAY: '#6c757d',
  GRAY_DARK: '#343a40',
  GRAY_LIGHT: '#dee2e6',
  GRAY_LIGHTER: '#f8f9fa',

  // Otros
  ORANGE: '#ff6b35',
  BLUE: '#0077be',
} as const;

export type ColorKey = keyof typeof COLORS;

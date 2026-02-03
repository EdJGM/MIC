/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        espe: {
          green: {
            primary: '#1a7b4e',
            dark: '#0d5c3a',
            light: '#2b9a68',
            lighter: '#e8f5f0',
          },
          orange: '#ff6b35',
          blue: '#0077be',
          gray: {
            dark: '#343a40',
            DEFAULT: '#6c757d',
            light: '#dee2e6',
            lighter: '#f8f9fa',
          }
        }
      },
      fontFamily: {
        sans: ['Segoe UI', 'Tahoma', 'Geneva', 'Verdana', 'sans-serif'],
      },
      boxShadow: {
        'sm': '0 2px 4px rgba(0, 0, 0, 0.08)',
        'md': '0 4px 6px rgba(0, 0, 0, 0.1)',
        'lg': '0 10px 15px rgba(0, 0, 0, 0.1)',
      },
      borderRadius: {
        'DEFAULT': '0.5rem',
        'lg': '0.75rem',
      },
      animation: {
        'fade-in': 'fadeIn 0.5s ease-out',
        'spin-slow': 'spin 1s linear infinite',
      },
      keyframes: {
        fadeIn: {
          '0%': { opacity: '0', transform: 'translateY(20px)' },
          '100%': { opacity: '1', transform: 'translateY(0)' },
        }
      }
    },
  },
  plugins: [],
}

import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <div class="app-container">
      <!-- Sidebar -->
      <aside class="sidebar">
        <div class="sidebar-header">
          <img src="assets/espe-logo.png" alt="ESPE Logo" class="logo-small" onerror="this.style.display='none'">
          <h4 class="sidebar-title">
            <i class="bi bi-diagram-3-fill"></i>
            Sistema de Gestión Integrado 
          </h4>
          <p class="sidebar-subtitle">SGI-ESPE</p>
        </div>

        <nav class="sidebar-nav">
          <div class="nav-section">
            <h6 class="nav-section-title">INVENTARIO DE PROCESOS</h6>
            
            <a class="nav-item" routerLink="/macroprocesos" routerLinkActive="active">
              <i class="bi bi-diagram-3"></i>
              <span>Macroprocesos</span>
            </a>
            
            <a class="nav-item" routerLink="/procesos" routerLinkActive="active">
              <i class="bi bi-diagram-2"></i>
              <span>Procesos</span>
            </a>
            
            <a class="nav-item" routerLink="/subprocesos" routerLinkActive="active">
              <i class="bi bi-diagram-2"></i>
              <span>Subprocesos</span>
            </a>
          </div>
          <div class="nav-section">
            <h6 class="nav-section-title">LISTA INFORMACION DOCUMENTADA</h6>
          </div>

        </nav>
      </aside>

      <!-- Main Content -->
      <main class="main-content">
        <!-- Top Header -->
        <header class="top-header">
          <div class="header-left">
            <button class="menu-toggle" (click)="toggleSidebar()">
              <i class="bi bi-list"></i>
            </button>
          </div>
          <div class="header-right">
            <span class="user-name">CACHIGUANGO, DENILSON</span>
            <div class="user-avatar">
              <i class="bi bi-person-circle"></i>
            </div>
          </div>
        </header>

        <!-- Page Content -->
        <div class="content-wrapper">
          <router-outlet></router-outlet>
        </div>
        <footer class="main-footer">
  <div class="footer-content">
    <div class="footer-left">
      <img src="assets/espe-logo.png" alt="ESPE" class="footer-logo" onerror="this.style.display='none'">
      <div class="footer-text">
        <p class="mb-0"><strong>Universidad de las Fuerzas Armadas ESPE</strong> 2025</p>
        <small>© Todos los derechos reservados.</small>
      </div>
    </div>
    <div class="footer-right">
      <small>Versión: 1.0.1</small>
    </div>
  </div>
</footer>
      </main>
    </div>
  `,
  styles: [`
  /* Variables de colores ESPE */
  :host {
    --espe-green: #1a7b4e;
    --espe-green-dark: #0d5c3a;
    --espe-green-light: #2b9a68;
    --espe-gray: #6c757d;
    --espe-light-gray: #f8f9fa;
    --sidebar-width: 280px;
  }

  .app-container {
    display: flex;
    min-height: 100vh;
    background-color: var(--espe-light-gray);
  }

  /* Sidebar Styles - AQUÍ ES DONDE MODIFICAS */
  .sidebar {
    width: var(--sidebar-width);
    background-image: 
      linear-gradient(180deg, rgba(234, 241, 238, 0.92) 0%, rgba(235, 241, 238, 0.92) 100%),
      url('/assets/espe-edificio.jpeg');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    color: white;
    display: flex;
    flex-direction: column;
    position: fixed;
    height: 100vh;
    overflow-y: auto;
    box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
    z-index: 1000;
    transition: transform 0.3s ease;
  }
  
  /* resto de estilos... */

    .sidebar-header {
      padding: 1.5rem;
      text-align: center;
      border-bottom: 1px solid rgba(14, 14, 14, 0.1);
    }

    
.header-logo-menu {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.logo-small {
  width: 45px;
  height: 45px;
  object-fit: contain;
}

    .sidebar-title {
      font-size: 1.1rem;
      font-weight: 600;
      margin: 0.5rem 0;
      color: black;
    }

    .sidebar-subtitle {
      font-size: 0.85rem;
      margin: 0;
      opacity: 0.9;
      color: black;
    }

    /* Navigation */
    .sidebar-nav {
      flex: 1;
      padding: 1rem 0;
      overflow-y: auto;
    }

    .nav-section {
      margin-bottom: 1.5rem;
    }

    .nav-section-title {
      font-size: 0.75rem;
      font-weight: 600;
      color: rgba(10, 10, 10, 0.7);
      padding: 0.5rem 1.5rem;
      margin: 0;
      text-transform: uppercase;
      letter-spacing: 1px;
    }

    .nav-item {
      display: flex;
      align-items: center;
      padding: 0.875rem 1.5rem;
      color: rgba(27, 27, 27, 0.9);
      text-decoration: none;
      transition: all 0.3s ease;
      font-size: 0.95rem;
      border-left: 3px solid transparent;
    }

    .nav-item i {
      font-size: 1.25rem;
      margin-right: 1rem;
      width: 24px;
      text-align: center;
    }

    .nav-item:hover {
      background-color: rgba(255, 255, 255, 0.1);
      color: black;
      border-left-color: white;
    }

    .nav-item.active {
      background-color: var(--espe-green);
      color: white;
      font-weight: 600;
      border-left-color: white;
    }

    /* Sidebar Footer */
    .sidebar-footer {
      padding: 1rem 1.5rem;
      border-top: 1px solid rgba(255, 255, 255, 0.1);
      background-color: rgba(0, 0, 0, 0.1);
    }

    .user-info {
      display: flex;
      align-items: center;
      margin-bottom: 1rem;
      font-size: 0.85rem;
      gap: 0.5rem;
    }

    .user-info i {
      font-size: 1.5rem;
    }

    .version-info {
      display: flex;
      flex-direction: column;
      font-size: 0.7rem;
      opacity: 0.8;
      gap: 0.25rem;
    }

    /* Main Content */
    .main-content {
      flex: 1;
      margin-left: var(--sidebar-width);
      display: flex;
      flex-direction: column;
      min-height: 100vh;
    }

    /* Top Header */
    .top-header {
      background: white;
      padding: 1rem 2rem;
      display: flex;
      justify-content: space-between;
      align-items: center;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08);
      position: sticky;
      top: 0;
      z-index: 100;
    }

    .header-left {
      display: flex;
      align-items: center;
      gap: 1rem;
    }

    .menu-toggle {
      background: none;
      border: none;
      font-size: 1.5rem;
      color: var(--espe-green);
      cursor: pointer;
      padding: 0.5rem;
      display: none;
    }

    .page-title {
      font-size: 1.25rem;
      font-weight: 600;
      color: var(--espe-green);
      margin: 0;
    }

    .header-right {
      display: flex;
      align-items: center;
      gap: 1rem;
    }

    .user-name {
      font-size: 0.9rem;
      color: var(--espe-gray);
      font-weight: 500;
    }

    .user-avatar {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      background: var(--espe-green-light);
      color: white;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 1.5rem;
    }

    /* Content Wrapper */
    .content-wrapper {
      flex: 1;
      padding: 2rem;
    }

    /* Responsive Design */
    @media (max-width: 992px) {
      .sidebar {
        transform: translateX(-100%);
      }

      .sidebar.show {
        transform: translateX(0);
      }

      .main-content {
        margin-left: 0;
      }

      .menu-toggle {
        display: block;
      }

      .user-name {
        display: none;
      }
    }

    @media (max-width: 576px) {
      .content-wrapper {
        padding: 1rem;
      }

      .top-header {
        padding: 1rem;
      }

      .sidebar {
        width: 100%;
      }

      :host {
        --sidebar-width: 100%;
      }
    }

    /* Scrollbar styling for sidebar */
    .sidebar::-webkit-scrollbar {
      width: 6px;
    }

    .sidebar::-webkit-scrollbar-track {
      background: rgba(255, 255, 255, 0.05);
    }

    .sidebar::-webkit-scrollbar-thumb {
      background: rgba(255, 255, 255, 0.2);
      border-radius: 3px;
    }

    .sidebar::-webkit-scrollbar-thumb:hover {
      background: rgba(255, 255, 255, 0.3);
    }
      /* Main Footer - AGREGAR AL FINAL */
  .main-footer {
    background: linear-gradient(135deg, var(--espe-green-dark) 0%, var(--espe-green) 100%);
    color: white;
    padding: 1.5rem 2rem;
    margin-top: auto;
    box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
  }

  .footer-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    max-width: 100%;
    flex-wrap: wrap;
    gap: 1rem;
  }

  .footer-left {
    display: flex;
    align-items: center;
    gap: 1rem;
  }

  .footer-logo {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    background: white;
    padding: 0.25rem;
  }

  .footer-text p {
    font-size: 0.95rem;
    margin-bottom: 0.25rem;
  }

  .footer-text small {
    font-size: 0.85rem;
    opacity: 0.9;
  }

  .footer-right {
    text-align: right;
  }

  .footer-right small {
    font-size: 0.85rem;
    background: rgba(255, 255, 255, 0.1);
    padding: 0.5rem 1rem;
    border-radius: 20px;
    font-weight: 500;
  }

  @media (max-width: 768px) {
    .footer-content {
      flex-direction: column;
      text-align: center;
    }
    
    .footer-left {
      flex-direction: column;
      text-align: center;
    }
    
    .footer-right {
      text-align: center;
    }
  }
    
  `]
})
export class AppComponent {
  title = 'Inventario de Procesos ESPE';
  sidebarVisible = true;

  toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    sidebar?.classList.toggle('show');
  }
}
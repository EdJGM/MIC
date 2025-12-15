import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
      <div class="container-fluid">
        <a class="navbar-brand" routerLink="/">
          <i class="bi bi-diagram-3-fill"></i> ESPE - Inventario de Procesos
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" 
                data-bs-target="#navbarNav" aria-controls="navbarNav" 
                aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav me-auto">
            <li class="nav-item">
              <a class="nav-link" routerLink="/macroprocesos" routerLinkActive="active">
                <i class="bi bi-diagram-3"></i> Macroprocesos
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/procesos" routerLinkActive="active">
                <i class="bi bi-flow-chart"></i> Procesos
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/subprocesos" routerLinkActive="active">
                <i class="bi bi-diagram-2"></i> Subprocesos
              </a>
            </li>
          </ul>
          <ul class="navbar-nav ms-auto">
            <li class="nav-item">
              <a class="nav-link" href="#">
                <i class="bi bi-person-circle"></i> Admin
              </a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
    
    <router-outlet></router-outlet>
  `,
  styles: [`
    .navbar-brand {
      font-weight: 600;
      font-size: 1.3rem;
    }
    
    .nav-link {
      font-weight: 500;
      transition: all 0.3s;
    }
    
    .nav-link:hover {
      background-color: rgba(255, 255, 255, 0.1);
      border-radius: 5px;
    }
    
    .nav-link.active {
      background-color: rgba(255, 255, 255, 0.2);
      border-radius: 5px;
      font-weight: 600;
    }
    
    .navbar {
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
  `]
})
export class AppComponent {
  title = 'Inventario de Procesos ESPE';
}
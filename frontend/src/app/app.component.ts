import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { SidebarModule } from 'primeng/sidebar';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { AvatarModule } from 'primeng/avatar';
import { MenuModule } from 'primeng/menu';
import { BadgeModule } from 'primeng/badge';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    SidebarModule,
    ButtonModule,
    RippleModule,
    AvatarModule,
    MenuModule,
    BadgeModule
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'SGI-ESPE';
  sidebarVisible = true;
  currentYear = new Date().getFullYear();

  // Estado de menús desplegables
  menuStates: { [key: string]: boolean } = {
    inventarioProcesos: false,
    listaInformacion: false,
    reportes: false
  };

  // Menú de usuario
  userMenuItems: MenuItem[] = [];

  ngOnInit(): void {
    this.initUserMenu();
  }

  initUserMenu(): void {
    this.userMenuItems = [
      {
        label: 'Configuración',
        icon: 'pi pi-cog',
        command: () => {
          // Navigate to config
        }
      },
      {
        separator: true
      },
      {
        label: 'Cerrar Sesión',
        icon: 'pi pi-sign-out',
        command: () => {
          // Logout logic
        }
      }
    ];
  }

  toggleSidebar(): void {
    this.sidebarVisible = !this.sidebarVisible;
  }

  toggleMenu(menuKey: string): void {
    this.menuStates[menuKey] = !this.menuStates[menuKey];
  }

  isMenuOpen(menuKey: string): boolean {
    return this.menuStates[menuKey] || false;
  }

  closeSidebar(): void {
    if (window.innerWidth < 1024) {
      this.sidebarVisible = false;
    }
  }
}
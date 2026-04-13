import { Component, OnInit, OnDestroy } from '@angular/core';
import { interval, Subscription } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { SidebarModule } from 'primeng/sidebar';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { AvatarModule } from 'primeng/avatar';
import { MenuModule } from 'primeng/menu';
import { BadgeModule } from 'primeng/badge';
import { MenuItem } from 'primeng/api';
import { NotificacionService } from './services/notificacion.service';

const USUARIO_SESION = 'admin-espe-001';

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
export class AppComponent implements OnInit, OnDestroy {
  title = 'SGI-ESPE';
  sidebarVisible = true;
  currentYear = new Date().getFullYear();
  cantidadNotificaciones = 0;

  private pollingSubscription?: Subscription;

  // Estado de menús desplegables
  menuStates: { [key: string]: boolean } = {
    inventarioProcesos: false,
    listaInformacion: false,
    reportes: false
  };

  // Menú de usuario
  userMenuItems: MenuItem[] = [];

  constructor(private notificacionService: NotificacionService) {}

  ngOnInit(): void {
    this.initUserMenu();
    this.cargarContadorNotificaciones();
  }

  cargarContadorNotificaciones(): void {
    this.notificacionService.contarNoLeidas(USUARIO_SESION).subscribe({
      next: (res) => { this.cantidadNotificaciones = res.noLeidas; },
      error: () => { this.cantidadNotificaciones = 0; }
    });

    // Refresca el contador cada 30 segundos
    this.pollingSubscription = interval(30000).pipe(
      switchMap(() => this.notificacionService.contarNoLeidas(USUARIO_SESION))
    ).subscribe({
      next: (res) => { this.cantidadNotificaciones = res.noLeidas; },
      error: () => {}
    });
  }

  ngOnDestroy(): void {
    this.pollingSubscription?.unsubscribe();
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
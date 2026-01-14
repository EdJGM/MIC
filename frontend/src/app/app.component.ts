import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Inventario de Procesos ESPE';
  sidebarVisible = true;
  inventarioProcesosOpen = false;
  listaInformacionDocumentadaOpen = false;

  toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    sidebar?.classList.toggle('show');
  }

  toggleInventarioProcesos() {
    this.inventarioProcesosOpen = !this.inventarioProcesosOpen;
  }

  toggleListaInformacionDocumentada() {
    this.listaInformacionDocumentadaOpen  = !this.listaInformacionDocumentadaOpen;

  }
}
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CardModule } from 'primeng/card';
import { ChartModule } from 'primeng/chart';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [CommonModule, RouterLink, CardModule, ChartModule],
    template: `
    <div class="space-y-6">
      <!-- Header -->
      <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-200">
        <h1 class="text-3xl font-bold text-gray-800 mb-2">
          Bienvenido al Sistema de Gestión Integrado
        </h1>
        <p class="text-gray-600">
          Universidad de las Fuerzas Armadas ESPE - Dashboard Principal
        </p>
      </div>

      <!-- Stats Cards -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <!-- Macroprocesos -->
        <div class="bg-gradient-to-br from-purple-500 to-indigo-600 rounded-xl shadow-lg p-6 text-white">
          <div class="flex items-center justify-between mb-4">
            <div>
              <p class="text-purple-100 text-sm font-medium">Macroprocesos</p>
              <h3 class="text-3xl font-bold">24</h3>
            </div>
            <div class="bg-white bg-opacity-20 p-3 rounded-lg">
              <i class="pi pi-sitemap text-2xl"></i>
            </div>
          </div>
          <div class="flex items-center gap-2">
            <i class="pi pi-arrow-up text-sm"></i>
            <span class="text-sm">12% vs mes anterior</span>
          </div>
        </div>

        <!-- Procesos -->
        <div class="bg-gradient-to-br from-blue-500 to-cyan-600 rounded-xl shadow-lg p-6 text-white">
          <div class="flex items-center justify-between mb-4">
            <div>
              <p class="text-blue-100 text-sm font-medium">Procesos</p>
              <h3 class="text-3xl font-bold">156</h3>
            </div>
            <div class="bg-white bg-opacity-20 p-3 rounded-lg">
              <i class="pi pi-th-large text-2xl"></i>
            </div>
          </div>
          <div class="flex items-center gap-2">
            <i class="pi pi-arrow-up text-sm"></i>
            <span class="text-sm">8% vs mes anterior</span>
          </div>
        </div>

        <!-- Subprocesos -->
        <div class="bg-gradient-to-br from-green-500 to-emerald-600 rounded-xl shadow-lg p-6 text-white">
          <div class="flex items-center justify-between mb-4">
            <div>
              <p class="text-green-100 text-sm font-medium">Subprocesos</p>
              <h3 class="text-3xl font-bold">342</h3>
            </div>
            <div class="bg-white bg-opacity-20 p-3 rounded-lg">
              <i class="pi pi-bars text-2xl"></i>
            </div>
          </div>
          <div class="flex items-center gap-2">
            <i class="pi pi-arrow-up text-sm"></i>
            <span class="text-sm">15% vs mes anterior</span>
          </div>
        </div>

        <!-- Documentos -->
        <div class="bg-gradient-to-br from-orange-500 to-red-600 rounded-xl shadow-lg p-6 text-white">
          <div class="flex items-center justify-between mb-4">
            <div>
              <p class="text-orange-100 text-sm font-medium">Documentos</p>
              <h3 class="text-3xl font-bold">89</h3>
            </div>
            <div class="bg-white bg-opacity-20 p-3 rounded-lg">
              <i class="pi pi-file-edit text-2xl"></i>
            </div>
          </div>
          <div class="flex items-center gap-2">
            <i class="pi pi-arrow-up text-sm"></i>
            <span class="text-sm">5% vs mes anterior</span>
          </div>
        </div>
      </div>

      <!-- Quick Actions -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <!-- Acciones Rápidas -->
        <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-200">
          <h2 class="text-xl font-bold text-gray-800 mb-4 flex items-center gap-2">
            <i class="pi pi-bolt text-indigo-600"></i>
            Acciones Rápidas
          </h2>
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <a 
              routerLink="/macroprocesos"
              class="flex items-center gap-3 p-4 rounded-lg border border-gray-200 hover:border-indigo-500 hover:bg-indigo-50 transition-all duration-200 cursor-pointer group">
              <div class="bg-indigo-100 group-hover:bg-indigo-500 p-2 rounded-lg transition-colors">
                <i class="pi pi-plus text-indigo-600 group-hover:text-white"></i>
              </div>
              <span class="font-medium text-gray-700 group-hover:text-indigo-600">
                Nuevo Macroproceso
              </span>
            </a>

            <a 
              routerLink="/procesos"
              class="flex items-center gap-3 p-4 rounded-lg border border-gray-200 hover:border-blue-500 hover:bg-blue-50 transition-all duration-200 cursor-pointer group">
              <div class="bg-blue-100 group-hover:bg-blue-500 p-2 rounded-lg transition-colors">
                <i class="pi pi-plus text-blue-600 group-hover:text-white"></i>
              </div>
              <span class="font-medium text-gray-700 group-hover:text-blue-600">
                Nuevo Proceso
              </span>
            </a>

            <a 
              routerLink="/subprocesos"
              class="flex items-center gap-3 p-4 rounded-lg border border-gray-200 hover:border-green-500 hover:bg-green-50 transition-all duration-200 cursor-pointer group">
              <div class="bg-green-100 group-hover:bg-green-500 p-2 rounded-lg transition-colors">
                <i class="pi pi-plus text-green-600 group-hover:text-white"></i>
              </div>
              <span class="font-medium text-gray-700 group-hover:text-green-600">
                Nuevo Subproceso
              </span>
            </a>

            <a 
              routerLink="/documentos"
              class="flex items-center gap-3 p-4 rounded-lg border border-gray-200 hover:border-orange-500 hover:bg-orange-50 transition-all duration-200 cursor-pointer group">
              <div class="bg-orange-100 group-hover:bg-orange-500 p-2 rounded-lg transition-colors">
                <i class="pi pi-file text-orange-600 group-hover:text-white"></i>
              </div>
              <span class="font-medium text-gray-700 group-hover:text-orange-600">
                Nuevo Documento
              </span>
            </a>
          </div>
        </div>

        <!-- Actividad Reciente -->
        <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-200">
          <h2 class="text-xl font-bold text-gray-800 mb-4 flex items-center gap-2">
            <i class="pi pi-clock text-indigo-600"></i>
            Actividad Reciente
          </h2>
          <div class="space-y-3">
            <div class="flex items-start gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors">
              <div class="bg-purple-100 p-2 rounded-lg">
                <i class="pi pi-plus text-purple-600 text-sm"></i>
              </div>
              <div class="flex-1">
                <p class="text-sm font-medium text-gray-800">
                  Nuevo macroproceso creado
                </p>
                <p class="text-xs text-gray-500 mt-1">
                  Vicerrectorado Docencia - Hace 2 horas
                </p>
              </div>
            </div>

            <div class="flex items-start gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors">
              <div class="bg-blue-100 p-2 rounded-lg">
                <i class="pi pi-pencil text-blue-600 text-sm"></i>
              </div>
              <div class="flex-1">
                <p class="text-sm font-medium text-gray-800">
                  Proceso actualizado
                </p>
                <p class="text-xs text-gray-500 mt-1">
                  Gestión Académica - Hace 4 horas
                </p>
              </div>
            </div>

            <div class="flex items-start gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors">
              <div class="bg-green-100 p-2 rounded-lg">
                <i class="pi pi-check text-green-600 text-sm"></i>
              </div>
              <div class="flex-1">
                <p class="text-sm font-medium text-gray-800">
                  Documento aprobado
                </p>
                <p class="text-xs text-gray-500 mt-1">
                  Reglamento Académico - Hace 1 día
                </p>
              </div>
            </div>

            <div class="flex items-start gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors">
              <div class="bg-orange-100 p-2 rounded-lg">
                <i class="pi pi-upload text-orange-600 text-sm"></i>
              </div>
              <div class="flex-1">
                <p class="text-sm font-medium text-gray-800">
                  Nuevo documento subido
                </p>
                <p class="text-xs text-gray-500 mt-1">
                  Manual de Procedimientos - Hace 2 días
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Estado de Documentación -->
      <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-200">
        <h2 class="text-xl font-bold text-gray-800 mb-6 flex items-center gap-2">
          <i class="pi pi-chart-bar text-indigo-600"></i>
          Estado General de Documentación
        </h2>
        <div class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-8 gap-4">
          <div class="text-center p-4 bg-gray-50 rounded-lg">
            <div class="text-2xl font-bold text-gray-400 mb-1">35</div>
            <div class="text-xs text-gray-600">No Documentado</div>
          </div>
          <div class="text-center p-4 bg-blue-50 rounded-lg">
            <div class="text-2xl font-bold text-blue-600 mb-1">42</div>
            <div class="text-xs text-blue-600">Levantamiento</div>
          </div>
          <div class="text-center p-4 bg-cyan-50 rounded-lg">
            <div class="text-2xl font-bold text-cyan-600 mb-1">38</div>
            <div class="text-xs text-cyan-600">Flujodiagramación</div>
          </div>
          <div class="text-center p-4 bg-yellow-50 rounded-lg">
            <div class="text-2xl font-bold text-yellow-600 mb-1">45</div>
            <div class="text-xs text-yellow-600">Caracterización</div>
          </div>
          <div class="text-center p-4 bg-orange-50 rounded-lg">
            <div class="text-2xl font-bold text-orange-600 mb-1">28</div>
            <div class="text-xs text-orange-600">Validación</div>
          </div>
          <div class="text-center p-4 bg-green-50 rounded-lg">
            <div class="text-2xl font-bold text-green-600 mb-1">52</div>
            <div class="text-xs text-green-600">Legalizado</div>
          </div>
          <div class="text-center p-4 bg-emerald-50 rounded-lg">
            <div class="text-2xl font-bold text-emerald-600 mb-1">61</div>
            <div class="text-xs text-emerald-600">Difundido</div>
          </div>
          <div class="text-center p-4 bg-purple-50 rounded-lg">
            <div class="text-2xl font-bold text-purple-600 mb-1">21</div>
            <div class="text-xs text-purple-600">Mejora</div>
          </div>
        </div>
      </div>
    </div>
  `,
    styles: [`
    :host {
      display: block;
    }
  `]
})
export class DashboardComponent implements OnInit {
    ngOnInit(): void {
        // Inicialización del dashboard
    }
}
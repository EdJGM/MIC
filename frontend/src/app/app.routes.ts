import { Routes } from '@angular/router';
import { MacroprocesosListComponent } from './components/macroprocesos-list/macroprocesos-list.component';
import { ProcesosListComponent } from './components/procesos-list/procesos-list.component';
import { SubprocesosListComponent } from './components/subprocesos-list/subprocesos-list.component';

export const routes: Routes = [
    { path: '', redirectTo: '/macroprocesos', pathMatch: 'full' },
    { path: 'macroprocesos', component: MacroprocesosListComponent },
    { path: 'procesos', component: ProcesosListComponent },
    { path: 'subprocesos', component: SubprocesosListComponent },
    { path: '**', redirectTo: '/macroprocesos' }
];
import { Routes } from '@angular/router';
import { ConfiguracionObjetivosComponent } from './components/configuracion-objetivos/configuracion-objetivos.component';
import { MacroprocesosListComponent } from './components/macroprocresos/macroprocresos.component';
import { ProcesosListComponent } from './components/procresos/procresos.component';
import { SubprocesosListComponent } from './components/subprocresos/subprocresos.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { RegistrosComponent } from './components/registros/registros.component';
import { DocumentosNormativosComponent } from './components/documentos-normativos/documentos-normativos.component';

export const routes: Routes = [
    { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
    { path: 'dashboard', component: DashboardComponent },
    { path: 'macroprocesos', component: MacroprocesosListComponent },
    { path: 'procesos', component: ProcesosListComponent },
    { path: 'subprocesos', component: SubprocesosListComponent },
    { path: 'registros', component: RegistrosComponent },
    { path: 'documentos', component: DocumentosNormativosComponent },
    // { path: 'reportes/procesos', component: ReportesProcesosComponent },
    // { path: 'reportes/documentos', component: ReportesDocumentosComponent },
    { path: 'configuracion-objetivos', component: ConfiguracionObjetivosComponent },
];
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentosNormativosComponent } from './documentos-normativos.component';

describe('DocumentosNormativosComponent', () => {
  let component: DocumentosNormativosComponent;
  let fixture: ComponentFixture<DocumentosNormativosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DocumentosNormativosComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DocumentosNormativosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

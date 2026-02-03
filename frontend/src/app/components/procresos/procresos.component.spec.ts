import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcresosComponent } from './procresos.component';

describe('ProcresosComponent', () => {
  let component: ProcresosComponent;
  let fixture: ComponentFixture<ProcresosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProcresosComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProcresosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

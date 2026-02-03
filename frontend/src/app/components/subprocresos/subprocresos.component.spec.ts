import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubprocresosComponent } from './subprocresos.component';

describe('SubprocresosComponent', () => {
  let component: SubprocresosComponent;
  let fixture: ComponentFixture<SubprocresosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubprocresosComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SubprocresosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

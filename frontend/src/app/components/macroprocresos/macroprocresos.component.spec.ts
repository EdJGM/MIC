import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MacroprocresosComponent } from './macroprocresos.component';

describe('MacroprocresosComponent', () => {
  let component: MacroprocresosComponent;
  let fixture: ComponentFixture<MacroprocresosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MacroprocresosComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MacroprocresosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

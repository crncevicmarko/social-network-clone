import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChpsformComponent } from './chpsform.component';

describe('ChpsformComponent', () => {
  let component: ChpsformComponent;
  let fixture: ComponentFixture<ChpsformComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChpsformComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChpsformComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

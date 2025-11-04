import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormalizateComponent } from './formalizate';

describe('Formalizate', () => {
  let component: FormalizateComponent;
  let fixture: ComponentFixture<FormalizateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormalizateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormalizateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

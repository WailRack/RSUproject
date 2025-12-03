import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScoringFormComponent } from './scoring-form';

describe('ScoringForm', () => {
  let component: ScoringFormComponent;
  let fixture: ComponentFixture<ScoringFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ScoringFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ScoringFormComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

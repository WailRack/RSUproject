import { Component, signal, Inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { NgIf, NgClass, PercentPipe, DecimalPipe } from '@angular/common';
import { ScoreResponse, ScoringService } from '../scoring.service';

@Component({
  selector: 'app-scoring-form',
  standalone: true,
  imports: [
    ReactiveFormsModule, 
    NgClass, 
    NgIf, 
    PercentPipe,
    DecimalPipe
  ],
  templateUrl: './scoring-form.html',
  styleUrl: './scoring-form.css',
})
export class ScoringFormComponent {
  form: FormGroup;
  loading = signal(false);
  error = signal<string | null>(null);
  result = signal<ScoreResponse | null>(null);
  isDark = signal(false);

  constructor(
    private readonly fb: FormBuilder,
    private readonly scoringService: ScoringService,
    @Inject(DOCUMENT) private readonly document: Document
  ) {
    this.form = this.fb.group({
      age: [null, [Validators.required, Validators.min(18), Validators.max(100)]],
      income: [null, [Validators.required, Validators.min(0)]],
      yearsAtJob: [null, [Validators.required, Validators.min(0)]],
      dependents: [0, [Validators.min(0)]],
      hasExistingLoan: [false],
      creditScore: [null, [Validators.required, Validators.min(300), Validators.max(900)]]
    });

    this.applyTheme();
  }

  toggleTheme() {
    this.isDark.set(!this.isDark());
    this.applyTheme();
  }

  private applyTheme() {
    const body = this.document.body;
    
    if (this.isDark()) {
      body.classList.remove('light');
      body.classList.add('dark');
    } else {
      body.classList.remove('dark');
      body.classList.add('light');
    }
  }

  submit(): void {
    this.error.set(null);
    this.loading.set(true);
    
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.loading.set(false);
      return;
    }

    this.scoringService.score(this.form.value).subscribe({
      next: (res) => {
        this.loading.set(false);
        this.result.set(res);
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set('Ошибка при запросе скоринга');
      }
    });
  }

  get f() {
    return this.form.controls;
  }
}
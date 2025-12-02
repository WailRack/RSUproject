import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


export interface ScoreRequest {
  age: number | null;
  income: number | null;
  yearsAtJob: number | null;
  dependents: number | null;
  hasExistingLoan: boolean | null;
  creditScore: number | null;
}

export interface ScoreResponse {
  ruleScore: number;
  mlProbability: number;
  approved: boolean;
}

@Injectable({
  providedIn: 'root',
})
export class ScoringService {

  private readonly baseUrl = 'http://localhost:8080/api/score';

  constructor(private readonly http: HttpClient) {}

  score(request: ScoreRequest): Observable<ScoreResponse> {
    return this.http.post<ScoreResponse>(this.baseUrl, request);
  }
  
}

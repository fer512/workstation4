import { CompanyStatus } from 'app/entities/enumerations/company-status.model';
import { Language } from 'app/entities/enumerations/language.model';

import { ICompany, NewCompany } from './company.model';

export const sampleWithRequiredData: ICompany = {
  id: 2126,
  name: 'Palladium Bicycle',
  status: 'ACTIVE',
  language: 'FRENCH',
};

export const sampleWithPartialData: ICompany = {
  id: 14677,
  name: 'ha kilogram Soap',
  status: 'DISABLED',
  language: 'SPANISH',
};

export const sampleWithFullData: ICompany = {
  id: 8160,
  name: 'cannibalize male',
  status: 'DISABLED',
  language: 'ENGLISH',
};

export const sampleWithNewData: NewCompany = {
  name: 'gold North',
  status: 'ACTIVE',
  language: 'SPANISH',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

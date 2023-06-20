import { BranchStatus } from 'app/entities/enumerations/branch-status.model';
import { Language } from 'app/entities/enumerations/language.model';

import { IBranch, NewBranch } from './branch.model';

export const sampleWithRequiredData: IBranch = {
  id: 71046,
  name: 'Plains orange',
  status: 'DISABLED',
  language: 'SPANISH',
};

export const sampleWithPartialData: IBranch = {
  id: 5577,
  name: 'Highlands',
  status: 'ACTIVE',
  language: 'FRENCH',
};

export const sampleWithFullData: IBranch = {
  id: 63835,
  name: 'Avon',
  status: 'ACTIVE',
  language: 'SPANISH',
};

export const sampleWithNewData: NewBranch = {
  name: 'Stage twang Southwest',
  status: 'ACTIVE',
  language: 'SPANISH',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

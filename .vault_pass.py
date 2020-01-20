#!/usr/bin/env python

import os
if 'VAULT_PASSWORD' in os.environ:
   if os.environ['VAULT_PASSWORD'] != '':
      print os.environ['VAULT_PASSWORD']
   else:
      print 'mot_de_passe_par_defaut'
else:
   print 'mot_de_passe_par_defaut'

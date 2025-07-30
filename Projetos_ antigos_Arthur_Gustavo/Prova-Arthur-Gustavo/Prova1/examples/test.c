#undef VALUE
#define VALUE 42
#ifdef SYSTEM1
#  include "system1.h"
#elif defined (SYSTEM2)
#  include <system2.h>
#  if ! defined (SUB)
     #error "Invalid subsystem"
   #endif
   #ifndef TESTE
     #define TESTE "Testing"
   #endif
#else
#  error "Invalid system"
#endif

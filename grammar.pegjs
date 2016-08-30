/*
MIT License

Copyright (c) 2016 Plankp T.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

{
  function iota(top) {
    top = top | 0 // Ensure `top` is a number
    if (top <= 1) return '[' + top + ']'
    var rst = new Array(top) // Alloc array by size
    for (var i = 0; i < top; i++) {
      rst[i] = i + 1;
    }
    return '[' + rst.join(',') + ']'
  }

  var id_func_counter = 0;
}

file
  = $1:expr $2:file?
  {
    return $1 + ($2 == null ? '' : ';\n' + $2)
  }

declr
  = DEFINE $1:Ident $2:func
  {
    return 'function ' + $1 + $2.substring(9)
  }
  / DEFINE $1:Ident $2:expr
  {
    return 'var ' + $1 + ' = (' + $2 + ')'
  }

DEFINE
  = '.def' 'ine'? _

expr
  = expr_3

expr_1
  = declr
  / $1:Ident
  {
    var func = $1
    switch (func.charAt(0)) {
    case '.':
      return func.substring(1)
    case '#':
      return func.replace(/#/g, '.')
    default:
      return func
    }
  }
  / Integer
  / func
  / $1:UNARY $2:expr
  {
    return $1 + $2
  }
  / CALL_OPEN $1:expr $2:expr* CALL_CLOSE
  {
    var sel = $1
    if (sel.charAt(0) == '.') return '(' + $2 + ')' + sel
    if (sel.startsWith('function ')) {
      var fname = sel.substring(9, sel.indexOf('('))
      return sel + fname + '(' + $2 + ')'
    }
    return '(' + sel + ')(' + $2 + ')'
  }
  / LIST_OPEN $2:expr* CALL_CLOSE
  {
    return '[' + $2 + ']'
  }
  / MAP_OPEN $1:map_pair* MAP_CLOSE
  {
    return '{' + $1 + '}'
  }

map_pair
  = $1:expr ',' _ $2:expr
  {
    return $1 + ':' + $2
  }

MAP_OPEN
  = '{'

MAP_CLOSE
  = '}'

LIST_OPEN
  = [`'] '(' _

mul_like_tail
  = $2:MUL_LIKE $3:expr_1
  {
    return $2 + '(' + $3 + ')'
  }

expr_2
  = $1:expr_1 $2:mul_like_tail+
  {
    return '(' + $1 + ')' + $2.join('')
  }
  / $1:expr_1 INDEXOF_OP $2:expr
  {
    return '(' + $1 + ')[' + $2 + ']'
  }
  / expr_1

INDEXOF_OP
  = '->' _

add_like_tail
  = $2:ADD_LIKE $3:expr_2
  {
    return $2 + '(' + $3 + ')'
  }

expr_3
  = $1:expr_2 $2:add_like_tail+
  {
    return '(' + $1 + ')' + $2.join('')
  }
  / expr_2

ADD_LIKE
  = $1:('+' / '-' / '&' / '|' / '<<' / '>>') _
  {
    return $1
  }

MUL_LIKE
  = $1:('%' / '*' / '/') _
  {
    return $1
  }

CALL_OPEN
  = $1:'(' _
  {
    return $1
  }

CALL_CLOSE
  = $1:')' _
  {
    return $1
  }

UNARY
  = $1:('-' / '!' / '~') _
  {
    return $1
  }

func
  = ID_FUNC
  / FUNC_OPEN $1:param_list? $2:expr FUNC_CLOSE
  {
    return ($1 == null ? 'function ()' : $1) + ' { return (' + $2 + ') }'
  }

FUNC_OPEN
  = '[' _

FUNC_CLOSE
  = ']' _

ID_FUNC
  = '_' _
  {
    var tmpname = '$' + id_func_counter++
    return 'function (' + tmpname + ') { return ' + tmpname + ' }'
  }

param_list
  = $1:Ident+ PARAM_INIT
  {
    return 'function (' + $1.join(',') + ')'
  }

PARAM_INIT
  = ':' _

Ident "ident"
  = [a-zA-Z] [a-zA-Z0-9$_]* _
  {
    return text().trim()
  }
  / ('.' _ Ident)+
  {
    return text().trim()
  }
  / ('#' _ Ident)+
  {
    return text().trim()
  }

Integer "integer"
  = '_'? ('0' / ([1-9][0-9]*)) _
  {
    var val = text().trim()
    var rst
    if (val.charAt(0) == '_') {
      rst = iota(parseInt(val.substring(1), 10))
    } else {
      rst = parseInt(val, 10) + ''
    }
    return rst
  }

_ "whitespace"
  = [ \t\n\r]*
  {
    return ' ';
  }

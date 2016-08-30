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
  function iota(top, conv_to_double) {
    top = top | 0 // Ensure `top` is a number
    if (top <= 1) return '[' + top + ']'
    var rst = []
    for (var i = 0; i < top; i++) {
      rst[i] = i + 1;
      if (conv_to_double) rst[i] += '.0'
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
  / boolean
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
  / string
  / Integer
  / func
  / $1:UNARY $2:expr
  {
    return $1 + $2
  }
  / CALL_OPEN $1:expr $2:expr* CALL_CLOSE
  {
    const b = $1
    const tail = $2
    if ('.' === b.charAt(0)) {
      if (tail.length > 1) {
        return '(' + tail[0] + ')' + b + '(' + tail.slice(1) + ')'
      }
      return '(' + tail + ')' + b
    }
    if (b.startsWith('function ')) {
      const c = b.substring(9, b.indexOf('('))
      if ('' !== c.trim()) {
        return b + c + '(' + tail + ')'
      }
    }
    return '(' + b + ')(' + tail + ')'
  }
  / LIST_OPEN $2:expr* CALL_CLOSE
  {
    return '[' + $2 + ']'
  }
  / MAP_OPEN $1:map_pair* MAP_CLOSE
  {
    return '{' + $1 + '}'
  }

boolean
  = $1:('true' / 'false') _
  {
    return $1
  }

string
  = '"' $1:str_char* '"' _
  {
    return '"' + $1.join('') + '"'
  }

str_char
  = unescaped
  / escaped

escaped
  = '\\' (('u' hex hex hex hex) / 'r' / 't' / 'b' / 'n' / 'f' / '\\' / '"')
  {
    return text()
  }

hex
  = [0-9a-fA-F]

/*
rule unescaped is copied from
https://medium.com/@daffl/beyond-regex-writing-a-parser-in-javascript-8c9ed10576a6
*/
unescaped
  = [\x20-\x21\x23-\x5B\x5D-\u10FFFF]

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

func_body
  = $2:expr $3:(';' _ expr)*
  {
    const tail = $3
    const tail_len = tail.length
    if (0 === tail_len) {
      return "return (" + $2 + ")";
    }
    for (var b = $2 + '; ', c = 0; c < tail_len - 1; c++) {
      b += tail[c][2], b += '; ';
    }
    return b + 'return (' + tail[tail_len - 1][2] + ')';
  }

func
  = ID_FUNC
  / FUNC_OPEN $1:param_list? $2:func_body? FUNC_CLOSE
  {
    return ($1 == null ? 'function ()' : $1) + ' { ' + ($2 == null ? '' : $2) + ' }'
  }

FUNC_OPEN
  = '[' _

FUNC_CLOSE
  = ']' _

ID_FUNC_CHAR
  = $1:'_' _
  {
    return $1
  }

ID_FUNC
  = ID_FUNC_CHAR
  {
    var tmpname = '$' + id_func_counter++
    return 'function (' + tmpname + ') { return ' + tmpname + ' }'
  }

param_list
  = $1:(Ident / ID_FUNC_CHAR)+ PARAM_INIT
  {
    var params = $1
    const params_len = params.length
    for (var i = 0; i < params_len; i++) {
      if (params[i] === '_') {
        var tmpname = '$' + id_func_counter++
        params[i] = tmpname
      }
    }
    return 'function (' + params.join(',') + ')'
  }

PARAM_INIT
  = ':' _

Ident "ident"
  = !boolean [a-zA-Z] [a-zA-Z0-9$_]* _
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
  = Float
  / '_'? ('0' / ([1-9][0-9]*)) _
  {
    const val = text().trim()
    var rst
    if (val.charAt(0) === '_') {
      rst = iota(parseInt(val.substring(1), 10), false)
    } else {
      rst = parseInt(val, 10) + ''
    }
    return rst
  }

Float "float"
  = '_' ('0' / ([1-9][0-9]*)) '.' '0'+ _
  {
    const val = text().trim()
    var rst
    if (val.charAt(0) === '_') {
      rst = iota(parseInt(val.substring(1), 10), true)
    } else {
      rst = parseInt(val, 10) + '.0'
    }
    return rst
  }
  / ('0' / ([1-9][0-9]*)) '.' [0-9]+ _
  {
    const val = text().trim()
    return val
  }

_ "whitespace"
  = [ \t\n\r]*
  {
    return ' '
  }

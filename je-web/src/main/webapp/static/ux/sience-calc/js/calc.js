var debugMode = false;
var isNotFirefox = true;
if (/Firefox[\/\s](\d+\.\d+)/.test(navigator.userAgent)) {
    isNotFirefox = false
}
function isMicrosoft(g) {
    return isBrowser("Microsoft", g)
}
function isBrowser(g, h) {
    browserOk = false;
    versionOk = false;
    browserOk = (navigator.appName.indexOf(g) != -1);
    if (h == 0) {
        versionOk = true
    } else {
        versionOk = (h <= parseInt(navigator.appVersion))
    }
    return browserOk && versionOk
}
var isIE = isMicrosoft(4);

function checkDouble(g) {
    if (isIE) {
        g.onmousedown()
    } else {
        return false
    }
}
function JGetId(g) {
    d = document;
    if (d.getElementById) {
        x = d.getElementById(g)
    } else {
        if (d.all) {
            x = d.all[g]
        }
    }
    return x
}
function RemoveNonLets(g, m) {
    var h, l, o = "";
    g = g.toUpperCase();
    m = m.toUpperCase();
    for (h = 0; h < g.length; h++) {
        l = g.charAt(h);
        if (m.indexOf(l) >= 0) {
            o += l
        }
    }
    return o
}
function RemoveLets(g, m) {
    var h, l, o = "";
    for (h = 0; h < g.length; h++) {
        l = g.charAt(h);
        if (m.indexOf(l) < 0) {
            o += l
        }
    }
    return o
}
var scrollTimer, DispHgt = 4,
    lc = -1,
    lmax = 0,
    lpos = 0;

function Complex(h, g) {
    this.r = h;
    this.i = g
}
function cNum(h, g) {
    return new Complex(h, g)
}
function rNum(g) {
    return new Complex(g, 0)
}
function iNum(g) {
    return new Complex(0, g)
}
tol = 1e-20;

function cvalMake(val) {
    var r = "0",
        i = "0",
        x = "0",
        y = "0";
    r = val;
    beg = val.indexOf("{");
    end = val.indexOf("}");
    if ((beg == 0) && (end > -1)) {
        end = Math.max(end, val.length - 1);
        r = val.substring(beg + 1, end);
        return rNum("{" + r + "}")
    }
    beg = val.indexOf("(");
    mid = val.indexOf(",");
    end = val.indexOf(")");
    if ((beg == 0) && (mid > -1) && (end > -1)) {
        end = Math.max(end, val.length - 1);
        r = val.substring(beg + 1, mid);
        i = val.substring(mid + 1, end);
        return cNum(eval(r), eval(i))
    }
    beg = val.indexOf("(");
    mid = val.indexOf("\u2220");
    end = val.indexOf(")");
    if ((beg == 0) && (mid > -1) && (end > -1)) {
        x = val.substring(1, mid);
        y = val.substring(mid + 1, val.length - 1);
        y = eval(y);
        yComp = cNum(y, 0);
        x = eval(x);
        xComp = cNum(x, 0);
        rComp = cNum(0, 0);
        iComp = cNum(0, 0);
        zComp = cNum(0, 0);
        zComp = Cos(yComp);
        rComp = Mult(xComp, zComp);
        iComp = Mult(xComp, (Sin(yComp)));
        r = rComp.r;
        i = iComp.r;
        return cNum(eval(r), eval(i))
    }
    mid = val.indexOf("+i");
    if (mid > -1) {
        x = val.substring(0, mid);
        y = val.substring(mid + 2, val.length);
        r = x;
        i = y
    }
    mid = val.indexOf("\u2220");
    if (mid > -1) {
        x = val.substring(0, mid);
        y = val.substring(mid + 1, val.length);
        if (AngUnit == "R") {
            y *= AngConst[AngUnit]
        } else {
            y *= (AngConst[AngUnit] * Math.PI)
        }
        r = eval(x) * Math.cos(eval(y));
        i = eval(x) * Math.sin(eval(y))
    }
    mid = val.indexOf("_b");
    if (mid > -1) {
        num = RemoveNonLets(val.substring(0, mid), digits);
        base = val.substring(mid + 2, val.length);
        sum = 0;
        for (k = 0; k < num.length; k++) {
            sum += Math.pow(base, num.length - 1 - k) * digits.indexOf(num.charAt(k))
        }
        r = sum
    }
    return cNum(eval(r), eval(i))
}
function isRnum(t) {
    var q, o = 0,
        h = "+",
        l, u, g, m;
    t = t.toLowerCase();
    for (q = 0; q < t.length; q++) {
        h = t.charAt(q);
        if (isrDigit(h)) {
            o = 1;
            break
        }
    }
    l = t.indexOf("e");
    g = t.indexOf("e", l + 1);
    if (g > l) {
        return 0
    }
    u = t.indexOf(".");
    m = t.indexOf(".", u + 1);
    if (m > u) {
        return 0
    }
    if ((l > -1) & (u > l)) {
        return 0
    }
    for (q = 0; q < t.length; q++) {
        lastx = h;
        h = t.charAt(q);
        if (q == 0) {
            o &= (isrSign(h) | isrDigit(h) | (h == "."))
        } else {
            if (q == (t.length - 1)) {
                o &= isrDigit(h)
            } else {
                if (lastx.toLowerCase() == "e") {
                    o &= (isrSign(h) | isrDigit(h))
                } else {
                    if (lastx == ".") {
                        o &= isrDigit(h)
                    } else {
                        o &= (isrDigit(h) | (h.toLowerCase() == "e") | (h == "."))
                    }
                }
            }
        }
    }
    return o
}
function isCnum(u) {
    var q, o, l, h, t, g, m = 0;
    l = u.indexOf("(");
    h = u.indexOf(",");
    t = u.lastIndexOf(",");
    g = u.indexOf(")");
    if ((l == 0) & (l < h) & (h == t) & (h < g) & (g == u.length - 1)) {
        q = u.substring(l + 1, h);
        o = u.substring(h + 1, g);
        if (isRnum(q) & isRnum(o)) {
            m = 1
        }
    }
    l = u.indexOf("(");
    h = u.indexOf("\u2220");
    t = u.lastIndexOf("\u2220");
    g = u.indexOf(")");
    if ((l == 0) & (l < h) & (h == t) & (h < g) & (g == u.length - 1)) {
        q = u.substring(l + 1, h);
        o = u.substring(h + 1, g);
        if (isRnum(q) & isRnum(o)) {
            m = 1
        }
    }
    l = 0;
    h = u.indexOf("+i");
    g = u.length - 1;
    if ((l <= h) & (h <= g)) {
        q = u.substring(l, h);
        o = u.substring(h + 2, g);
        if (((q == "") | (isRnum(q))) & ((o == "") | (isRnum(o)))) {
            m = 1
        }
    }
    return m
}
function formatNum(o, g, l) {
    if (l == null) {
        var u, h = calcPrec,
            A = SFormatValue;
        switch (A) {
        case 1:
            o = formatNumNice(o, 12);
            o = o.replace("+", "");
            break;
        case 2:
            o = o.toFixed(h).toString();
            break;
        case 3:
            h++;
            o = toExp(o, h);
            break;
        case 4:
            h++;
            o = toExp(o, h);
            var w = o.indexOf("e"),
                v = o.substr(0, w),
                t = o.substr(w + 1, o.length);
            var q = ((t % 3) + 3) % 3;
            var z = o.indexOf(".") + q;
            var v = v.replace(".", "");
            newnum = v.substr(0, z) + "." + v.substr(z, v.length);
            t = t - q;
            o = "" + newnum + "e" + t;
            break
        }
    } else {
        o = formatNumNice(o, 15);
        o = o.replace("+", "")
    }
    return o.toString()
}
function getWidth(m, g) {
    var l = document.createElement("span");
    l.style.whiteSpace = "nowrap";
    l.className = "testWidth";
    l.style.fontSize = g + "px";
    l.innerHTML = m;
    document.body.appendChild(l);
    var h = l.offsetWidth;
    document.body.removeChild(l);
    return h
}
function formatNumNice(g, h) {
    var m = 0;
    var l = 0;
    if (g == 0) {
        m = "0"
    } else {
        var l = Math.floor(Math.log(Math.abs(g)) / Math.log(10));
        if (l >= 12 || l <= -4) {
            m = g.toExponential(h)
        } else {
            m = g.toFixed(h - l)
        }
    }
    m = stripZeros(m);
    return m
}
function formatNumStore(g, l) {
    var m = 0;
    var h = "";
    h = g.toString();
    if (h.indexOf("e") != -1) {
        m = g.toExponential(l)
    } else {
        m = g.toFixed(l)
    }
    return m
}
function formatNumNiceComp(h, l) {
    var o = 0;
    var m = 0;
    var g = 0;
    if (h == 0) {
        o = "0"
    } else {
        var m = Math.floor(Math.log(Math.abs(h)) / Math.log(10));
        if (m >= 12 || m <= -4) {
            m = m.toString();
            g = m.length;
            o = h.toExponential(l - g)
        } else {
            o = h.toFixed(l - Math.abs(m))
        }
    }
    o = stripZeros(o);
    return o
}
function stripZeros(h) {
    if (h.indexOf(".") != -1) {
        if (h.indexOf("e") != -1) {
            var g = h.split("e");
            h = g[0];
            while (h.charAt(h.length - 1) == "0") {
                h = h.substr(0, h.length - 1)
            }
            if (h.charAt(h.length - 1) == ".") {
                h = h.substr(0, h.length - 1)
            }
            h = h + "e" + g[1]
        } else {
            while (h.charAt(h.length - 1) == "0") {
                h = h.substr(0, h.length - 1)
            }
            if (h.charAt(h.length - 1) == ".") {
                h = h.substr(0, h.length - 1)
            }
        }
    }
    return h
}
function toExp(u, h) {
    u = parseFloat(u);
    if (u == Number.NaN) {
        return u
    }
    if (u == Infinity) {
        return u
    }
    if (u == NaN) {
        return u
    }
    var t = u.toPrecision(h);
    if (t.indexOf("e") > -1) {
        t = t.replace("+", "");
        return t
    }
    if (Number(t) == 0) {
        return t + "e0"
    }
    var v = Number(t) > 0 ? 0 : 1;
    var l = Math.abs(Number(t)) >= 1 ? 0 : 1;
    var o = String(t).indexOf(".");
    if (l) {
        var g = String(t).search(/[1-9]/);
        var q = -(g - o)
    } else {
        if (o > -1) {
            q = o - 1 - v
        } else {
            q = t.length - 1
        }
    }
    if (l) {
        var w = t.slice(-h, -h + 1) + "." + t.slice(-h + 1) + "e" + q
    } else {
        var m = t.replace(".", "");
        m = m.replace("-", "");
        var w = m.slice(0, 1) + "." + m.slice(1) + "e" + q
    }
    if (v) {
        w = "-" + w
    }
    return w
}
function cShow(m) {
    var l, h, g;
    l = m.r;
    g = Math.round(l);
    if (Math.abs(l - g) < tol) {
        l = g
    }
    h = m.i;
    g = Math.round(h);
    if (Math.abs(h - g) < tol) {
        h = g
    }
    return "(" + l + "+" + h + "i)"
}
function cShowPair(t, h) {
    var q = t.r,
        m = t.i,
        l, o, g;
    o = formatNum(q, false, h);
    if (m == 0) {
        return o
    }
    o = formatNum(q, true, h);
    g = formatNum(m, true, h);
    if (SModeValue == 1) {
        temp = "(" + o + "+" + g + "i)";
        temp = temp.replace("+-", "-");
        return temp
    } else {
        return "(" + o + "," + g + ")"
    }
}
function cShowPairInfix(u) {
    var g = u.r,
        t = u.i,
        h, v, m;
    var l = formatNumStore(g, 12);
    var q = formatNumStore(t, 12);
    var o;
    o = Math.abs(g) - Math.abs(l);
    if (Math.abs(o) < 1e-14) {
        g = l
    }
    o = Math.abs(t) - Math.abs(q);
    if (Math.abs(o) < 1e-14) {
        t = q
    }
    return "(" + g + "," + t + ")"
}
function cShowPolar(t, h) {
    var q = Abs(t).r,
        m = Arg(t).r,
        l, o, g;
    o = formatNum(q, false, h);
    if (t.i == 0) {
        return o
    }
    o = formatNum(q, true, h);
    g = formatNum(m, true, h);
    return "(" + o + "\u2220" + g + ")"
}
function cShowPhasor(g) {
    return "" + Abs(g).r + "e^(" + Arg(g).r + "i)"
}
Zero = cNum(0, 0);
One = cNum(1, 0);
Two = cNum(2, 0);
I = cNum(0, 1);
cNaN = cNum(NaN, NaN);
digits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

function iscEq(h, g) {
    if ((h.r == g.r) && (h.i == g.i)) {
        return 1
    }
    return 0
}
function isRe(g) {
    if ((g.i == 0)) {
        return 1
    }
    return 0
}
function isIm(g) {
    if ((g.r == 0) && (g.i != 0)) {
        return 1
    }
    return 0
}
function iscNaN(g) {
    if (isNaN(g.r) || isNaN(g.i)) {
        return 1
    }
    return 0
}
function iscInt(g) {
    if ((g.r == Math.floor(g.r)) && (g.i == Math.floor(g.i))) {
        return 1
    }
    return 0
}
function isrInt(g) {
    if ((isRe(g)) && (iscInt(g))) {
        return 1
    }
    return 0
}
function rriiio(h, g, l) {
    if (isrInt(h) && isrInt(g)) {
        return Round(l)
    } else {
        return (l)
    }
}
function criiio(h, g, l) {
    if (iscInt(h) && isrInt(g)) {
        return Round(l)
    } else {
        return (l)
    }
}
function cpriiio(h, g, l) {
    if (iscInt(h) && isrInt(g) && (g.r > 0)) {
        return Round(l)
    } else {
        return (l)
    }
}
function cciiio(h, g, l) {
    if (iscInt(h) && iscInt(g)) {
        return Round(l)
    } else {
        return (l)
    }
}
function isExp(g) {
    if (g.toString().indexOf("e") > -1) {
        return 1
    }
    return 0
}
function GetDLen(t) {
    var q, m, g, l, o, h;
    q = (t.r).toString();
    g = q.indexOf("e");
    if (g > -1) {
        q = q.substr(1, g - 1)
    }
    l = q.indexOf(".");
    o = 0;
    if (l > -1) {
        o = q.length - l - 1
    }
    m = (t.i).toString();
    g = m.indexOf("e");
    if (g > -1) {
        m = m.substr(1, g - 1)
    }
    l = m.indexOf(".");
    h = 0;
    if (l > -1) {
        h = m.length - l - 1
    }
    return cNum(o, h)
}
function GetSciExp(t) {
    var q, m, g, l, o, h;
    q = (t.r).toString();
    g = q.indexOf("e");
    if (g > -1) {
        q = q.substr(g + 1, q.length)
    }
    m = (t.i).toString();
    g = m.indexOf("e");
    if (g > -1) {
        m = m.substr(g + 1, q.length)
    }
    return cNum(q, m)
}
function Re(g) {
    return rNum(g.r)
}
function Im(g) {
    return rNum(g.i)
}
function Sgn(g) {
    r = g.r;
    if (r != 0) {
        r = r / Math.abs(r)
    }
    i = g.i;
    if (i != 0) {
        i = i / Math.abs(i)
    }
    return cNum(r, i)
}
function Conj(g) {
    if (g.i == 0) {
        return cNum(g.r, 0)
    } else {
        return cNum(g.r, -g.i)
    }
}
function Neg(g) {
    if (g.i == 0) {
        return cNum(-g.r, 0)
    } else {
        if (g.r == 0) {
            return cNum(0, -g.i)
        } else {
            return cNum(-g.r, -g.i)
        }
    }
}
function Add(q, m) {
    var g = q.r + m.r;
    var u = q.i + m.i;
    var o = cNum(q.r, q.i);
    var t = cNum(m.r, m.i);
    o.r = parseFloat(formatNumStore(o.r, 12));
    o.i = parseFloat(formatNumStore(o.i, 12));
    t.r = parseFloat(formatNumStore(t.r, 12));
    t.i = parseFloat(formatNumStore(t.i, 12));
    var l = o.r + t.r;
    var h = o.i + t.i;
    if (g != 0) {
        if (l == 0) {
            q.r = o.r;
            m.r = t.r
        }
    }
    if (u != 0) {
        if (h == 0) {
            q.i = o.i;
            m.i = t.i
        }
    }
    return cNum(q.r + m.r, q.i + m.i)
}
function Sub(h, g) {
    return Add(h, Neg(g))
}
function Mult(l, h) {
    if (isRe(l) && isRe(h)) {
        return cNum((l.r * h.r), 0)
    } else {
        var o = l.r * h.r;
        var m = l.i * h.i;
        var t = l.r * h.i;
        var q = l.i * h.r;
        o = cleanFloat(o);
        m = cleanFloat(m);
        t = cleanFloat(t);
        q = cleanFloat(q);
        var u = cNum(0, 0);
        var g = cNum(0, 0);
        u = Sub(cNum(o, 0), cNum(m, 0));
        g = Add(cNum(t, 0), cNum(q, 0));
        var v = cNum(u.r, g.r);
        return cNum(v.r, v.i)
    }
}
function AbsSqr(g) {
    return Mult(g, Conj(g))
}
function Div(h, g) {
    if (isRe(h) && isRe(g)) {
        return cNum((h.r / g.r), 0)
    } else {
        n = Mult(h, Conj(g));
        d = AbsSqr(g).r;
        return cNum(n.r / d, n.i / d)
    }
}
function Abs(g) {
    return rNum(Math.sqrt(AbsSqr(g).r))
}
function ArgRad(g) {
    return rNum(Math.atan2(g.i, g.r))
}
function Arg(g) {
    if (AngUnit == "R") {
        return rNum(Math.atan2(g.i, g.r) / AngConst[AngUnit])
    } else {
        return rNum(Math.atan2(g.i, g.r) / (AngConst[AngUnit]) / (Math.PI))
    }
}
function Polar(g) {
    return cNum(Abs(g).r, Arg(g).r)
}
function Exp(g) {
    if (g.r == -Infinity) {
        return Zero
    }
    exp = Math.exp(g.r);
    cos = Math.cos(g.i);
    sin = Math.sin(g.i);
    d = g.i / Math.PI;
    if (Math.abs(d - Math.round(d)) == 0) {
        sin = 0
    }
    d += 0.5;
    if (Math.abs(d - Math.round(d)) == 0) {
        cos = 0
    }
    d = g.i / (2 * Math.PI);
    if (Math.abs(d - Math.round(d)) == 0) {
        cos = 1
    }
    return cNum(exp * cos, exp * sin)
}
function Ln(g) {
    return cNum(Math.log(Abs(g).r), ArgRad(g).r)
}
function Pow(h, g) {
    if (isRe(h) && isRe(g)) {
        return cNum(Math.pow(h.r, g.r), 0)
    } else {
        if (iscEq(h, Zero)) {
            if (iscEq(g, Zero)) {
                return One
            }
            if ((g.r < 0) && (g.i == 0)) {
                return cNaN
            }
            return Zero
        }
        a = Exp(Mult(Ln(h), g));
        return cpriiio(h, g, a)
    }
}
function Inv(g) {
    return Pow(g, Neg(One))
}
function Logx(h, g) {
    return Div(Ln(h), Ln(g))
}
function Root(h, g) {
    return Exp(Div(Ln(h), g))
}
function Sqr(g) {
    return Mult(g, g)
}
function Sqrt(g) {
    return Root(g, Two)
}
function Log(g) {
    return Logx(g, rNum(10))
}
function Flr(g) {
    return cNum(Math.floor(g.r), Math.floor(g.i))
}
function Round(g) {
    return cNum(Math.round(g.r), Math.round(g.i))
}
function Ceil(g) {
    return cNum(Math.ceil(g.r), Math.ceil(g.i))
}
function Int(g) {
    r = (g.r >= 0) ? Math.floor(g.r) : Math.ceil(g.r);
    i = (g.i >= 0) ? Math.floor(g.i) : Math.ceil(g.i);
    return cNum(r, i)
}
function Frac(g) {
    return Sub(g, Int(g))
}
AngUnit = "R";
AngConst = new Array();
AngConst.C = 2 * Math.PI;
AngConst.R = 1;
AngConst.D = 1 / 180;
AngConst.G = 1 / 200;

function AngIn(h) {
    var g;
    if (AngUnit == "R") {
        g = cNum(h.r, h.i)
    } else {
        g = Mult(Mult(h, rNum(AngConst[AngUnit])), rNum(Math.PI))
    }
    return g
}
function AngOut(h) {
    var g;
    if (AngUnit == "R") {
        g = cNum(h.r, h.i)
    } else {
        g = Div(Mult(h, rNum(1 / AngConst[AngUnit])), rNum(Math.PI))
    }
    return g
}
function AngCnvt(g) {
    g = Re(c)
}
function Sin(g) {
    return Mult(I, Sinh(Mult(Neg(I), g)))
}
function Cos(g) {
    return Cosh(Mult(Neg(I), g))
}
function Tan(g) {
    return Div(Sin(g), Cos(g))
}
function Csc(g) {
    return Inv(Sin(g))
}
function Sec(g) {
    return Inv(Cos(g))
}
function Cot(g) {
    return Inv(Tan(g))
}
function Asin(g) {
    return AngOut(Neg(Mult(I, Ln(Add(Mult(I, g), Sqrt(Sub(One, Sqr(g))))))))
}
function Acos(g) {
    return AngOut(Neg(Mult(I, Ln(Add(g, Mult(I, Sqrt(Sub(One, Sqr(g)))))))))
}
function Atan(h) {
    var g = AngOut(Div(Mult(I, Ln(Div(Sub(One, Mult(I, h)), Add(One, Mult(I, h))))), Two));
    if (h.i == 0) {
        g.i = 0
    }
    return g
}
function Acsc(g) {
    return Asin(Inv(g))
}
function Asec(g) {
    return Acos(Inv(g))
}
function Acot(g) {
    return Atan(Inv(g))
}
function Sinh(g) {
    return Div(Sub(Exp(AngIn(g)), Exp(Neg(AngIn(g)))), Two)
}
function Cosh(h) {
    var g;
    g = Div(Add(Exp(AngIn(h)), Exp(Neg(AngIn(h)))), Two);
    return g
}
function Tanh(g) {
    return Div(Sinh(g), Cosh(g))
}
function Csch(g) {
    return Inv(Sinh(g))
}
function Sech(g) {
    return Inv(Cosh(g))
}
function Coth(g) {
    return Inv(Tanh(g))
}
function Asinh(g) {
    return AngOut(Ln(Add(g, Sqrt(Add(One, Sqr(g))))))
}
function Acosh(g) {
    return AngOut(Ln(Add(g, Mult(Sqrt(Add(g, One)), Sqrt(Sub(g, One))))))
}
function Atanh(g) {
    return AngOut(Div(Sub(Ln(Add(One, g)), Ln(Sub(One, g))), Two))
}
function Acsch(g) {
    return Asinh(Inv(g))
}
function Asech(g) {
    return Acosh(Inv(g))
}
function Acoth(g) {
    return Atanh(Inv(g))
}
function IDiv(h, g) {
    return rNum(Math.floor(h.r / g.r))
}
function Mod(h, g) {
    return rNum(h.r % g.r)
}
function Gcd(h, g) {
    a = h.r;
    b = g.r;
    if (b == 0) {
        return rNum(a)
    } else {
        return Gcd(rNum(b), rNum(a % b))
    }
}
function Lcm(h, g) {
    return Div(Mult(h, g), Gcd(h, g))
}
function Factor(g) {
    n = g.r;
    ns = Math.sqrt(n) + 1;
    out = "";
    if (n < 0) {
        n = -n;
        out = "-1,"
    }
    while ((n % 2 == 0) && (n != 0)) {
        n /= 2;
        out += "2,"
    }
    for (k = 3; k <= n; k = k + 2) {
        if ((n % k) == 0) {
            n /= k;
            out += k + ",";
            if (n == 1) {
                break
            }
            ns = Math.sqrt(n) + 1;
            k -= 2
        } else {
            if (k > ns) {
                out += n + ",";
                break
            }
        }
    }
    if (out == "" || out == "-1,") {
        out += n + ","
    }
    return rNum("{" + out.substr(0, out.length - 1) + "}")
}
function Prime(g) {
    n = g.r;
    p = new Array();
    p[1] = 2;
    p[2] = 3;
    i = 2;
    chk = p[i] + 2;
    while (i < n) {
        for (j = 1; j <= i; j++) {
            if (chk % p[j] == 0) {
                chk += 2;
                break
            }
            if (j == i) {
                i++;
                p[i] = chk
            }
        }
    }
    return rNum(p[n])
}
function Prime2(g) {
    n = g.r;
    sz = 20 * n;
    while (true) {
        pcnt = 1;
        sptr = 1;
        sieve = new Array();
        for (i = 2; i < sz; i++) {
            sieve[i] = 1
        }
        while (sptr < sz) {
            sptr++;
            if (sieve[sptr] > 0) {
                pcnt++;
                sieve[sptr] = pcnt;
                if (pcnt > n) {
                    return rNum(sptr)
                }
                for (i = 2 * sptr; i < sz; i = i + sptr) {
                    sieve[i] = 0
                }
            }
        }
        sz *= 2
    }
}
function LnGamma(g) {
    cof = new Array(76.18009172947146, -86.50532032941678, 24.01409824083091, -1.231739572450155, 0.001208650973866179, -0.000005395239384953);
    y = g;
    x = g;
    tmp = Add(x, rNum(5.5));
    tmp = Sub(tmp, Mult(Add(x, rNum(0.5)), Ln(tmp)));
    ser = rNum(1.000000000190015);
    for (j = 0; j <= 5; j++) {
        y = Add(y, One);
        ser = Add(ser, Div(rNum(cof[j]), y))
    }
    return Add(Neg(tmp), Ln(Mult(rNum(2.5066282746310007), Div(ser, x))))
}
function Gamma(g) {
    if ((isrInt(g)) && (g.r > 0)) {
        p = 1;
        for (n = 1; n < g.r; n++) {
            p *= n
        }
        return rNum(p)
    }
    return Exp(LnGamma(g))
}
function Factorial(g) {
    if (isrInt(g) && (g.r > 0) && (g.r < 100)) {
        return ProdABx(One, g)
    } else {
        if (g.r < 101) {
            return Gamma(Add(g, One))
        }
    }
}
function Beta(h, g) {
    return Div(Mult(Gamma(h), Gamma(g)), Gamma(Add(h, g)))
}
function ProdABx(g, m) {
    var l = One,
        h;
    for (h = Math.floor(g.r); h <= Math.floor(m.r); h++) {
        l = Mult(l, cNum(h, 0))
    }
    return l
}
function Perm(h, g) {
    return ProdABx(rNum(h.r - g.r + 1), rNum(h.r))
}
function Comb(h, g) {
    sml = Math.min(g.r, (h.r - g.r));
    return Div(ProdABx(rNum(h.r - sml + 1), rNum(h.r)), Factorial(rNum(sml)))
}
function Rand(g) {
    return cNum(g.r * Math.random(), g.i * Math.random())
}
function Base(h, g) {
    num = h.r;
    base = g.r;
    if ((base < 2) || (base > 36)) {
        return cNaN
    }
    sym = "_b" + base;
    while (num > 0) {
        mod = num % base;
        sym = digits.charAt(mod) + sym;
        num = Math.floor(num / base)
    }
    return rNum(sym)
}
function b_NOT(g) {
    return rNum(~g.r)
}
function b_ShfR(h, g) {
    return rNum(h.r >> g.r)
}
function b_ShfL(h, g) {
    return rNum(h.r << g.r)
}
function b_XOR(h, g) {
    return rNum(h.r ^ g.r)
}
function b_AND(h, g) {
    return rNum(h.r & g.r)
}
function b_OR(h, g) {
    return rNum(h.r | g.r)
}
function b_NAND(h, g) {
    return rNum(~ (h.r & g.r))
}
function b_NOR(h, g) {
    return rNum(~ (h.r | g.r))
}
function Bnot(g) {
    return rNum(~g.r)
}
function Bxor(h, g) {
    return rNum(h.r ^ g.r)
}
function Band(h, g) {
    return rNum(h.r & g.r)
}
function Bor(h, g) {
    return rNum(h.r | g.r)
}
function Bnand(h, g) {
    return rNum(~ (h.r & g.r))
}
function Bnor(h, g) {
    return rNum(~ (h.r | g.r))
}
function l_NOT(g) {
    return rNum(!g.r)
}
function l_AND(h, g) {
    return rNum(h.r && g.r)
}
function l_OR(h, g) {
    return rNum(h.r || g.r)
}
function l_XOR(h, g) {
    return rNum(h.r != g.r)
}
function l_NAND(h, g) {
    return rNum(!(h.r && g.r))
}
function l_NOR(h, g) {
    return rNum(!(h.r || g.r))
}
function Lnot(g) {
    return rNum(!g.r)
}
function Land(h, g) {
    return rNum(h.r && g.r)
}
function Lor(h, g) {
    return rNum(h.r || g.r)
}
function Lxor(h, g) {
    return rNum(h.r != g.r)
}
function Lnand(h, g) {
    return rNum(!(h.r && g.r))
}
function Lnor(h, g) {
    return rNum(!(h.r || g.r))
}
function OnesComp(g) {
    return b_NOT(g)
}
function TwosComp(g) {
    return Add(OnesComp(g), One)
}
function toJSlist(g) {
    return RemoveLets(g, "}{")
}
function Min(c) {
    x = toJSlist(c.r);
    return rNum(eval("Math.min(" + x + ")"))
}
function Max(c) {
    x = toJSlist(c.r);
    return rNum(eval("Math.max(" + x + ")"))
}
function Cnt(g) {
    x = toJSlist(g.r);
    x = x.split(",");
    return rNum(x.length)
}
function Sum(c) {
    x = toJSlist(c.r);
    x = x.split(",");
    s = 0;
    for (k = 0; k < x.length; k++) {
        s += eval(x[k])
    }
    return rNum(s)
}
function Prod(c) {
    x = toJSlist(c.r);
    x = x.split(",");
    s = 1;
    for (k = 0; k < x.length; k++) {
        s *= eval(x[k])
    }
    return rNum(s)
}
function Avg(g) {
    return Div(Sum(g), Cnt(g))
}
function Variance(c) {
    x = toJSlist(c.r);
    x = x.split(",");
    av = Avg(c).r;
    N = Cnt(c).r;
    s = 0;
    for (k = 0; k < x.length; k++) {
        s += Math.pow(eval(x[k]) - av, 2)
    }
    s /= N;
    return rNum(s)
}
function StdDev(g) {
    return Sqrt(Variance(g))
}
function ToJoeFrac(q) {
    var h, o = GetDLen(q),
        g = Pow(rNum(10), rNum(o.r)),
        l = Mult(q, g),
        m = Gcd(l, g);
    l = Div(l, m);
    g = Div(g, m);
    h = rNum(l.r + "/" + g.r);
    if (cShowPair(h).length > 24) {
        return q
    }
    if (g.r == 1) {
        return rNum(l.r)
    }
    return h
}
function ToJoeMixed(u) {
    var q = Int(u);
    var m = Frac(u);
    var h, t = GetDLen(m),
        g = Pow(rNum(10), rNum(t.r)),
        l = Mult(m, g),
        o = Gcd(l, g);
    l = Div(l, o);
    g = Div(g, o);
    m = rNum(l.r + "/" + g.r);
    h = rNum(q.r + "+" + m.r);
    if (cShowPair(h).length > 24) {
        return u
    }
    if (l.r == 0) {
        return rNum(q.r)
    }
    return h
}
function IntPow(h, g) {
    ans = One;
    b = h;
    e = g.r;
    while (e > 0) {
        f = e % 2;
        if (f == 1) {
            ans = Mult(ans, b)
        }
        e = Math.floor(e / 2);
        b = Sqr(b)
    }
    return ans
}
function Attn() {
    if (((dStack < 2) || (dFloat == 3)) && (activeInput != "")) {
        JGetId(activeInput).focus()
    }
    return
}
function AllClear() {
    if ((dStack < 2) || (dFloat == 3)) {
        var g = new RegExp("unitInput");
        if (g.test(activeInput) == true) {
            for (j = 0; j <= 11; j++) {
                for (i = 0; i <= 11; i++) {
                    unitStore[j][i] = "";
                    document.getElementById("unitInput" + i).value = ""
                }
            }
        } else {
            if ((activeInput == "hex_box") || (activeInput == "dec_box") || (activeInput == "oct_box")) {
                clearBases()
            } else {
                if (activeInput == "ecalc_precision") {
                    calcPrec = "";
                    updatePrecision()
                } else {
                    if (activeInput == "cellInputBox") {
                        activeInput = "ecalc_stack0";
                        showCursor();
                        exitSolver()
                    } else {
                        if (activeInput == "dToFInput") {
                            document.getElementById(activeInput).value = "";
                            calc()
                        } else {
                            if (activeInput != "") {
                                lc = -1;
                                lmax = 0;
                                recallIndex = -1;
                                for (i = -DispHgt; i < 1; i++) {
                                    inList[i] = "<br>";
                                    expList[i] = "";
                                    stk[i] = Zero
                                }
                                DoMath("", 0, "0");
                                activeInput = "ecalc_stack0";
                                Attn()
                            }
                        }
                    }
                }
            }
        }
    }
}
function OFchk(g) {
    if (g.length >= 26) {
        return g + "O!"
    }
    return g
}
function UpdateDisp(g) {
    for (i = g - DispHgt + 1; i <= g; i++) {
        if (SCoordValue == 1) {
            inList[i] = cShowPair(stk[i])
        } else {
            if ((SCoordValue == 2)) {
                if (stk[i].i == 0) {
                    inList[i] = cShowPair(stk[i])
                } else {
                    inList[i] = cShowPolar(stk[i])
                }
            }
        }
    }
    if (SModeValue == 1) {
        for (i = 1; i <= DispHgt; i++) {
            if ((expList[g - i + 1]) != "") {
                testSize = inList[g - i + 1];
                newFontSize = 22;
                j = 0;
                temp = getWidth(testSize, newFontSize);
                while ((temp > 333) && (j <= 6)) {
                    newFontSize--;
                    temp = getWidth(testSize, newFontSize);
                    j++
                }
                JGetId("ecalc_stack" + (i).toString()).style.fontSize = newFontSize + "px";
                JGetId("ecalc_stack" + (i).toString()).innerHTML = inList[g - i + 1];
                testSize = expList[g - i + 1];
                newFontSize = 22;
                j = 0;
                temp = getWidth(testSize, newFontSize);
                while ((temp > 333) && (j <= 6)) {
                    newFontSize--;
                    temp = getWidth(testSize, newFontSize);
                    j++
                }
                JGetId("ecalc_stack" + (i + 1).toString()).style.fontSize = newFontSize + "px";
                JGetId("ecalc_stack" + (i + 1).toString()).innerHTML = expList[g - i + 1];
                i += 1;
                g += 1
            } else {
                testSize = inList[g - i + 1];
                newFontSize = 22;
                j = 0;
                temp = getWidth(testSize, newFontSize);
                while ((temp > 333) && (j <= 6)) {
                    newFontSize--;
                    temp = getWidth(testSize, newFontSize);
                    j++
                }
                JGetId("ecalc_stack" + (i).toString()).style.fontSize = newFontSize + "px";
                JGetId("ecalc_stack" + (i).toString()).innerHTML = inList[g - i + 1];
                JGetId("ecalc_stack" + (i + 1).toString()).innerHTML = "";
                i += 1;
                g += 1
            }
        }
    } else {
        for (i = 1; i <= DispHgt; i++) {
            testSize = inList[g - i + 1];
            newFontSize = 22;
            j = 0;
            temp = getWidth(testSize, newFontSize);
            while ((temp > 333) && (j <= 6)) {
                newFontSize--;
                temp = getWidth(testSize, newFontSize);
                j++
            }
            JGetId("ecalc_stack" + (i).toString()).style.fontSize = newFontSize + "px";
            JGetId("ecalc_stack" + (i).toString()).innerHTML = inList[g - i + 1]
        }
    }
    return
}
function DoChangeSign() {
    var g = JGetId("ecalc_stack0").value;
    if (g != "") {
        changeSign()
    } else {
        stk[lc] = Neg(stk[lc]);
        UpdateDisp(lc)
    }
    return
}
JSmath = new Array("abs", "acos", "asin", "atan", "atan2", "ceil", "cos", "E", "exp", "floor", "LN10", "LN2", "log", "LOG10E", "LOG2E", "max", "min", "PI", "pow", "random", "round", "sin", "sqrt", "SQRT1_2", "SQRT2", "tan");
JSnum = "0123456789";

function DoMath(op, optype, inStr) {
    var inp = "";
    if (inStr == null) {
        inp = JGetId("ecalc_stack0").value
    } else {
        inp = inStr
    }
    if (SModeValue == 2) {
        inp = sciPrefix(inp)
    }
    try {
        dStack += 1;
        if ((dStack >= 2) && (dFloat != 3)) {
            enableD()
        }
        if ((inp == "") & (op == "")) {
            inp = cShowPairInfix(stk[lpos])
        }
        val = "      " + inp;
        tmpval = "";
        for (k = 0; k < val.length; k++) {
            addon = val.charAt(k);
            addonlen = 0;
            for (jsk = 0; jsk < JSmath.length; jsk++) {
                if ((val.indexOf(JSmath[jsk], k) == k) && (val.indexOf("Math.", k - 5) != k - 5)) {
                    addon = "Math." + JSmath[jsk];
                    addonlen = JSmath[jsk].length - 1
                }
            }
            tmpval += addon;
            k += addonlen
        }
        val = "";
        for (k = 0; k < tmpval.length; k++) {
            let = tmpval.charAt(k);
            if (let != " ") {
                val += let
            }
        }
        if (val.toUpperCase() == "I") {
            val = "(0,1)"
        }
        if (val != "") {
            cval = cvalMake(val)
        } else {
            cval = stk[lc]
        }
        opr = op;
        if (op == "+") {
            opr = "Add"
        }
        if (op == "-") {
            opr = "Sub"
        }
        if (op == "*") {
            opr = "Mult"
        }
        if (op == "x") {
            opr = "Mult"
        }
        if (op == "/") {
            opr = "Div"
        }
        if (op == "!") {
            opr = "Factorial"
        }
        if (inp != "") {
            switch (optype) {
            case 0:
                z1 = "null";
                z2 = "cval";
                nxt = 1;
                break;
            case 1:
                z1 = "cval";
                z2 = "null";
                nxt = 1;
                break;
            case 2:
                z1 = "stk[lc]";
                z2 = "cval";
                nxt = 0;
                break
            }
        } else {
            switch (optype) {
            case 0:
                z1 = "null";
                z2 = "stk[lc]";
                nxt = 0;
                break;
            case 1:
                z1 = "stk[lc]";
                z2 = "null";
                nxt = 0;
                break;
            case 2:
                z1 = "stk[lc-1]";
                z2 = "stk[lc]";
                nxt = -1;
                break
            }
        }
        endFlag = 0;
        var stkPoint;
        if ((lc + nxt) >= 0) {
            stkPoint = lc + nxt;
            stk[lc + nxt] = eval(opr + "(" + z1 + "," + z2 + ")");
            lc += nxt
        } else {
            stkPoint = lc;
            stk[lc] = eval(opr + "(" + z1 + "," + z2 + ")");
            endFlag = 1
        }
        var fDiff;
        var r = stk[stkPoint].r;
        var r2 = parseFloat(formatNumStore(r, 12));
        fDiff = Math.abs(r) - Math.abs(r2);
        if (Math.abs(fDiff) < 1e-14) {
            r = r2
        }
        var i = stk[stkPoint].i;
        var i2 = parseFloat(formatNumStore(i, 12));
        fDiff = Math.abs(i) - Math.abs(i2);
        if (Math.abs(fDiff) < 1e-14) {
            i = i2
        }
        stk[stkPoint].r = r;
        stk[stkPoint].i = i;
        if (SModeValue == 2) {
            if (inp != "") {
                switch (optype) {
                case 0:
                    z1 = "null";
                    z2 = "cval";
                    nxt = 1;
                    break;
                case 1:
                    makeUnaryExp(op, inp);
                    break;
                case 2:
                    makeBinExp(op, inp, 0);
                    break
                }
            } else {
                switch (optype) {
                case 0:
                    z1 = "null";
                    z2 = "stk[lc]";
                    nxt = 0;
                    break;
                case 1:
                    makeUnaryExp(op, inp);
                    break;
                case 2:
                    makeBinExp(op, inp, endFlag);
                    break
                }
            }
        }
        inList[lc] = "";
        if (optype == 0) {
            inList[lc] += cShowPair(cval)
        }
        if (op != "") {
            inList[lc] += cShowPair(stk[lc])
        }
        lpos = lc;
        lmax = lc;
        UpdateDisp(lpos);
        clearInputBox()
    } catch (err) {
        return
    }
    return
}
function cleanFloat(h) {
    var g;
    var l;
    l = parseFloat(formatNumStore(h, 12));
    g = Math.abs(h) - Math.abs(l);
    if (Math.abs(g) < 1e-14) {
        h = l
    }
    return h
}
Array.prototype.indexOf = function(h) {
    for (var g = 0; g < this.length; g++) {
        if (this[g] === h) {
            return g
        }
    }
    return -1
};
rsign = "+-";
rdigits = "0123456789";
digits = "0123456789.eE";
extdigits = digits + "+-";
lets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
c_xor = String.fromCharCode(164);
c_nor = String.fromCharCode(166);
c_nand = String.fromCharCode(167);
c_not = String.fromCharCode(172);
b_ops = "*/%+-><&|^=" + c_xor + c_nor + c_nand;
rgtacting_u_ops = "~" + c_not;
lftacting_u_ops = "!";
u_ops = rgtacting_u_ops + lftacting_u_ops;

function isrSign(g) {
    return (rsign.indexOf(g) > (-1))
}
function isrDigit(g) {
    return (rdigits.indexOf(g) > (-1))
}
function isDigit(g) {
    return (digits.indexOf(g) > (-1))
}
function isExtDigit(g) {
    return (extdigits.indexOf(g) > (-1))
}
function isLet(g) {
    return (lets.indexOf(g.toUpperCase()) > (-1))
}
function isFnc(g) {
    return (lets.indexOf(g.toUpperCase()) > (-1)) | (u_ops.indexOf(g) > (-1))
}
function isOp(g) {
    return (b_ops.indexOf(g) > (-1))
}
function prcdnc(h) {
    OpArr = new Array("~", "!", "^", "*", "/", "%", "+", "-", "<<", ">>", ">>>", "<", "<=", ">", ">=", "==", "!=", "===", "!==", "&", "|", "&&", "||", "=");
    OpPrcArr = new Array(14, 14, 14, 13, 13, 13, 12, 12, 11, 11, 11, 10, 10, 10, 10, 9, 9, 9, 9, 8, 6, 5, 4, 2);
    if (isFnc(h)) {
        return 15
    }
    var g = OpArr.indexOf(h);
    if (g > -1) {
        return OpPrcArr[g]
    }
    return 0
}
String.prototype.left = function(g) {
    return (g > this.length) ? this : this.substring(0, g)
};
String.prototype.right = function(g) {
    return (g > this.length) ? this : this.substring(this.length - g)
};
String.prototype.toProper = function() {
    var g = this.toLowerCase();
    return g.left(1).toUpperCase() + g.right(g.length - 1)
};

function replaceAll(l, h, g) {
    return l.replace(new RegExp(h, "g"), g)
}
function UpdateStr(str, cond, repl) {
    var z = "+",
        prevz = "+",
        nextz = "+",
        tstr = "",
        lastz = str.length - 1,
        i;
    for (i = 0; i < str.length; i++) {
        prevz = z;
        z = str.charAt(i);
        nextz = str.charAt(i + 1);
        if (eval(cond)) {
            tstr += repl;
            if (tstr.right(1) == "z") {
                tstr = tstr.substring(0, tstr.length - 1) + z
            }
        } else {
            tstr += z
        }
    }
    return tstr
}
function Ans(g) {
    return stk[lc]
}
function Infix2RPN(u) {
    if (debugMode) {
        JGetId("test2").value = ""
    }
    var m, l, h, g = "",
        v = "+",
        q = "";
    var w = new Array;
    expList[lc + 1] = u;
    u = sciPrefix(u);
    u = u.toLowerCase();
    u = u.replace(/e\^/g, "(2.718281828459045)^");
    u = replaceAll(u, "pi", "(3.141592653589793)");
    u = replaceAll(u, "ans", "(ans(1))");
    u = replaceAll(u, "exp", "Exp");
    u = UpdateStr(u, "(z=='i')&(!isLet(prevz))&((!isLet(nextz))|(i==(lastz)))", "(0,1)");
    u = UpdateStr(u, "((prevz==')')|isrDigit(prevz))&((z=='(')|( (isFnc(z))&(z!='!')&(z!='e')))", "*z");
    u = UpdateStr(u, "((prevz==')')&((z=='(')|isDigit(z)))", "*z");
    v = "+";
    q = "";
    for (m = 0; m < u.length; m++) {
        var t = "";
        for (j = 0; j < w.length; j++) {
            t += w[j] + " "
        }
        if (debugMode) {
            JGetId("test2").value += g + " : " + t + "\n"
        }
        lastx = v;
        v = u.charAt(m);
        nextx = u.charAt(m + 1);
        if (v == " ") {
            v = lastx;
            continue
        }
        if ((v.toLowerCase() == "e") & isDigit(lastx)) {
            g += v;
            continue
        }
        if (((v == "+") | (v == "-")) & (lastx.toLowerCase() == "e")) {
            g += v;
            v = "e";
            continue
        }
        if (isDigit(v) & (lastx.toLowerCase() == "e")) {
            g += v;
            continue
        }
        if ((v == "-") && ((isOp(lastx)) || (m == 0))) {
            g += "-1 ";
            w[w.length] = "*";
            v = "0";
            continue
        }
        if ((v == "-") && ((isOp(lastx)) || (lastx == "(")) && (nextx == "(")) {
            g += "-1";
            w[w.length] = "*";
            v = "0";
            continue
        }
        if (((v == "+") | (v == "-")) & ((lastx == "(") | (lastx == ",") | isOp(lastx))) {
            g += v;
            v = "0";
            continue
        }
        if (v == ".") {
            for (j = g.length; j >= 0; j--) {
                if ((j == 0) || (g.charAt(j) == " ")) {
                    g += v;
                    break
                }
                if ((g.charAt(j) == "e") | (!isDigit(g.charAt(j))) && (!isOp(g.charAt(j)))) {
                    return "Invalid expression"
                }
            }
            continue
        }
        if (isDigit(lastx) & !isDigit(v) & !((lastx == "e") & (v == "-"))) {
            g += " "
        }
        if (isDigit(v) & isFnc(lastx)) {
            w[w.length - 1] += v;
            v = lastx;
            continue
        }
        if (isFnc(v)) {
            if (isFnc(lastx)) {
                w[w.length - 1] += v
            } else {
                w.push(v)
            }
            continue
        }
        if (isDigit(v)) {
            g += v;
            continue
        }
        if (isOp(v)) {
            if ((m == 0) & (v == "-")) {
                g += v
            } else {
                if (isOp(lastx)) {
                    w[w.length - 1] += v
                } else {
                    while ((w.length > 0)) {
                        if ((prcdnc(v) > prcdnc(w[w.length - 1]))) {
                            break
                        }
                        g += w.pop() + " "
                    }
                    w.push(v)
                }
            }
            continue
        }
        if (v == "(") {
            l = u.indexOf(")", m);
            h = u.substring(m, l + 1);
            if (!isFnc(lastx) & isCnum(h)) {
                g += h + " ";
                m = l;
                v = "0"
            } else {
                w.push(v)
            }
            continue
        }
        if (v == ")") {
            while ((w[w.length - 1] != "(")) {
                if (w.length == 0) {
                    return "Invalid expression"
                }
                g += w.pop() + " "
            }
            w.pop();
            if (w.length > 0) {
                if (w[w.length - 1].length > 1) {
                    g += w.pop() + " "
                }
            }
            continue
        }
    }
    if (debugMode) {
        JGetId("test2").value += g + " : " + t + "\n=======================\n"
    }
    while (w.length > 0) {
        lastx = v;
        v = w.pop();
        if (isDigit(lastx) & !isDigit(v)) {
            g += " "
        }
        if (debugMode) {
            JGetId("test2").value += g + "\n"
        }
        g += v + " "
    }
    if (debugMode) {
        JGetId("test2").value += g + "\n"
    }
    return g
}
function DoMathBTS(str) {
    try {
        if (str == "") {
            return ""
        }
        var inp = str;
        var inpArr = inp.split(" ");
        var stkBTS = new Array;
        binops = "+-*/^PowRootAddSubMultDivMod&|" + c_xor + c_nor + c_nand;
        var ans = x = 0;
        for (i = 0; i < inpArr.length; i++) {
            x = inpArr[i].toProper();
            opr = x;
            if (x == "+") {
                opr = "Add"
            }
            if (x == "-") {
                opr = "Sub"
            }
            if (x == "*") {
                opr = "Mult"
            }
            if (x == "/") {
                opr = "Div"
            }
            if (x == "^") {
                opr = "Pow"
            }
            if (x == "!") {
                opr = "Factorial"
            }
            if (x == "&") {
                opr = "Band"
            }
            if (x == "|") {
                opr = "Bor"
            }
            if (x == c_xor) {
                opr = "Bxor"
            }
            if (x == c_nor) {
                opr = "Bnor"
            }
            if (x == c_nand) {
                opr = "Bnand"
            }
            ans = 0;
            if ((x == "") | (x == " ")) {
                if (debugMode) {
                    JGetId("test2").value += stkBTS + "\n"
                }
                continue
            }
            if (binops.indexOf(x) > -1) {
                c2 = cvalMake(stkBTS.shift());
                c1 = cvalMake(stkBTS.shift());
                ans = cShowPairInfix(eval(opr + "(c1,c2)"));
                stkBTS.unshift("" + ans);
                if (debugMode) {
                    JGetId("test2").value += stkBTS + "\n"
                }
                continue
            }
            if (!(isRnum(x) | isCnum(x))) {
                c1 = cvalMake(stkBTS.shift());
                ans = cShowPairInfix(eval(opr + "(c1)"));
                stkBTS.unshift("" + ans);
                if (debugMode) {
                    JGetId("test2").value += stkBTS + "\n"
                }
                continue
            }
            if (isRnum(x) | isCnum(x)) {
                stkBTS.unshift(x);
                if (debugMode) {
                    JGetId("test2").value += stkBTS + "\n"
                }
                continue
            }
        }
    } catch (err) {
        return false
    }
    return stkBTS[0]
}
function swap() {
    var g;
    g = stk[lpos];
    stk[lpos] = (stk[lpos - 1]);
    stk[lpos - 1] = g;
    g = expList[lpos];
    expList[lpos] = expList[lpos - 1];
    expList[lpos - 1] = g;
    UpdateDisp(lpos)
}
function evalSolverNums() {
    document.getElementById("cellInputBox").value = "";
    for (iSlv = 0;
    (iSlv < (solverSize * solverSize)); iSlv++) {
        tempS = cvalMake(DoMathBTS(Infix2RPN(solverArrayA[iSlv])));
        solverArrayA2[iSlv] = "(" + tempS.r + "," + tempS.i + ")"
    }
    for (iSlv = 0; iSlv < solverSize; iSlv++) {
        tempS = cvalMake(DoMathBTS(Infix2RPN(solverArrayB[iSlv])));
        solverArrayB2[iSlv] = "(" + tempS.r + "," + tempS.i + ")"
    }
    iSlv = 0
}
function LSsolve() {
    var g, h, q = solverSize;

    function o() {
        var z, v, t, w;
        for (z = 0; z < q; z++) {
            var u = z;
            for (v = z; v < q; v++) {
                if (AbsSqr(g[v][z]) > AbsSqr(g[u][z])) {
                    u = v
                }
            }
            for (t = z; t < q + 1; t++) {
                w = g[z][t];
                g[z][t] = g[u][t];
                g[u][t] = w
            }
            for (v = z + 1; v < q; v++) {
                for (t = q; t > z; t--) {
                    if (iscEq(g[z][z], Zero)) {
                        return false
                    } else {
                        g[v][t] = Sub(g[v][t], Div(Mult(g[z][t], g[v][z]), g[z][z]))
                    }
                }
            }
        }
        return true
    }
    function l() {
        var u, t, v;
        for (u = q - 1; u >= 0; u--) {
            v = Zero;
            for (t = u + 1; t < q; t++) {
                v = Add(v, Mult(g[u][t], h[t]))
            }
            h[u] = Div(Sub(g[u][q], v), g[u][u])
        }
    }
    var m;
    g = new Array();
    for (r = 0; r < q; r++) {
        g[r] = new Array()
    }
    h = new Array();
    for (r = 0; r < q; r++) {
        for (c = 0; c < q; c++) {
            m = solverArrayA2[r * q + c];
            if (m == "") {
                m = "0"
            }
            g[r][c] = cvalMake(m)
        }
        m = solverArrayB2[r];
        if (m == "") {
            m = "0"
        }
        g[r][q] = cvalMake(m);
        h[r] = Zero
    }
    chk = false;
    for (c = 0; c < q; c++) {
        for (r = 0; r < q; r++) {
            chk = chk || iscNaN(g[r][c])
        }
        chk = chk || iscNaN(h[c])
    }
    if (chk) {
        window.alert("Invalid Input!");
        return
    }
    if (o()) {
        l();
        for (c = 0; c < q; c++) {
            JGetId("solverOutput" + (c + 1)).innerHTML = cShowPair(h[c])
        }
    } else {
        window.alert("Singular Matrix!")
    }
}
function CPolySolve() {
    var h = j = k = 0;
    var g = 40;
    ConvergeIter = 40;
    var l = new Array();
    var o = new Array();
    var q = new Array();
    var t = new Array();
    var v = "";
    CPSize = solverSize;
    for (c = CPSize; c >= 0; c--) {
        fVal = solverArrayRoot[c];
        if (fVal == "") {
            fVal = "0"
        }
        l[c] = cvalMake(fVal)
    }
    for (h = 0; h < CPSize; h++) {
        o[h] = Zero;
        q[h] = Add(Rand(cNum(2, 2)), cNum(-1, -1))
    }
    maxOrd = CPSize;
    for (ord = maxOrd; ord >= 0; ord--) {
        CPSize = ord;
        if (!iscEq(l[ord], Zero)) {
            break
        }
    }
    if (CPSize <= 0) {
        v += "No Roots";
        return 0
    }
    for (num = 0; num < CPSize; num++) {
        Num0Roots = num;
        if (!iscEq(l[num], Zero)) {
            break
        }
        v += "0\n";
        if (num == CPSize - 1) {
            return
        }
    }
    CPSize -= Num0Roots;
    for (c = 0; c <= CPSize; c++) {
        l[c] = l[c + Num0Roots]
    }
    div = l[CPSize];
    for (c = CPSize; c >= 0; c--) {
        l[c] = Div(l[c], div)
    }
    var u = Zero,
        m = One;
    for (k = 0; k <= g; k++) {
        for (h = 0; h < CPSize; h++) {
            o[h] = q[h]
        }
        for (h = 0; h < CPSize; h++) {
            u = Zero;
            m = One;
            zn = One;
            for (j = 0; j <= CPSize; j++) {
                u = Add(u, Mult(l[j], zn));
                zn = Mult(zn, o[h])
            }
            for (j = 0; j < CPSize; j++) {
                if (Math.abs(j - h) > 0.01) {
                    m = Mult(m, Sub(o[h], o[j]))
                }
            }
            q[h] = Sub(o[h], Div(u, m))
        }
        Converged = 1;
        for (h = 0; h < CPSize; h++) {
            Converged &= (AbsSqr(Sub(o[h], q[h]))).r < 1e-20
        }
        if (Converged && (ConvergeIter == g)) {
            ConvergeIter = k
        }
        if (k >= ConvergeIter + 2) {
            break
        }
        if (k == g) {
            v = "Timed Out\nHere is last set\n"
        }
    }
    for (h = 0; h < CPSize; h++) {
        if (Math.abs(q[h].i) < 1e-20) {
            q[h].i = 0
        }
        if (Math.abs(q[h].r) < 1e-20) {
            q[h].r = 0
        }
        t[h] = cShowPair(q[h])
    }
    for (h = CPSize; h < solverSize; h++) {
        t[h] = "0"
    }
    t.sort();
    for (h = 0; h < t.length; h++) {
        v += t[h] + "\n";
        JGetId("solverOutput" + (h + 1)).innerHTML = t[h]
    }
    v += "\n" + k + " iters"
}
function checkMainInput() {
    if ((activeInput == "ecalc_stack0") || (activeInput == "cellInputBox")) {
        return true
    } else {
        return false
    }
}
function checkSolver() {
    if ((SModeValue == 1) || (activeInput == "cellInputBox")) {
        return true
    } else {
        return false
    }
}
function key_asin() {
    if (checkMainInput()) {
        if (checkSolver()) {
            numPressed("asin(")
        } else {
            DoMath("Asin", 1)
        }
    }
    return isNotFirefox
}
function key_acos() {
    if (checkMainInput()) {
        if (checkSolver()) {
            numPressed("acos(")
        } else {
            DoMath("Acos", 1)
        }
    }
    return isNotFirefox
}
function key_atan() {
    if (checkMainInput()) {
        if (checkSolver()) {
            numPressed("atan(")
        } else {
            DoMath("Atan", 1)
        }
    }
    return isNotFirefox
}
function key_comma() {
    if (checkMainInput()) {
        if (SModeValue == 1) {
            numPressed("i")
        } else {
            numPressed(",")
        }
    }
    return isNotFirefox
}
function key_angle() {
    if (checkMainInput()) {
        numPressed("\u2220")
    }
    return isNotFirefox
}
function key_fact() {
    if (checkMainInput()) {
        if (checkSolver()) {
            numPressed("!")
        } else {
            DoMath("Factorial", 1)
        }
    }
    return isNotFirefox
}
function key_sin() {
    if (checkMainInput()) {
        if (checkSolver()) {
            numPressed("sin(")
        } else {
            DoMath("Sin", 1)
        }
    }
    return isNotFirefox
}
function key_cos() {
    if (checkMainInput()) {
        if (checkSolver()) {
            numPressed("cos(")
        } else {
            DoMath("Cos", 1)
        }
    }
    return isNotFirefox
}
function key_tan() {
    if (checkMainInput()) {
        if (checkSolver()) {
            numPressed("tan(")
        } else {
            DoMath("Tan", 1)
        }
    }
    return isNotFirefox
}
function key_lbrack() {
    if (checkMainInput()) {
        numPressed("(")
    }
    return isNotFirefox
}
function key_rbrack() {
    if (checkMainInput()) {
        numPressed(")")
    }
    return isNotFirefox
}
function key_ans() {
    if (checkMainInput()) {
        if (checkSolver()) {
            numPressed("Ans")
        } else {
            swap()
        }
    }
    return isNotFirefox
}
function key_pi() {
    if (checkMainInput()) {
        if (checkSolver()) {
            numPressed("pi")
        } else {
            numPressed("3.141592653589793")
        }
    }
    return isNotFirefox
}
function key_yx() {
    if (checkMainInput()) {
        if (checkSolver()) {
            if ((JGetId("ecalc_stack0").value == "") && (activeInput != "cellInputBox")) {
                numPressed("Ans^")
            } else {
                numPressed("^")
            }
        } else {
            DoMath("Pow", 2)
        }
    }
    return isNotFirefox
}
function key_squared() {
    if (checkMainInput()) {
        if (checkSolver()) {
            if ((JGetId("ecalc_stack0").value == "") && (activeInput != "cellInputBox")) {
                numPressed("Ans^2")
            } else {
                numPressed("^2")
            }
        } else {
            DoMath("Sqr", 1)
        }
    }
    return isNotFirefox
}
function key_sqrt() {
    if (checkMainInput()) {
        if (checkSolver()) {
            numPressed("sqrt(")
        } else {
            DoMath("Sqrt", 1)
        }
    }
    return isNotFirefox
}
function key_ex() {
    if (checkMainInput()) {
        if (checkSolver()) {
            numPressed("e^(")
        } else {
            DoMath("Exp", 1)
        }
    }
    return isNotFirefox
}
function key_ln() {
    if (checkMainInput()) {
        if (checkSolver()) {
            numPressed("ln(")
        } else {
            DoMath("Ln", 1)
        }
    }
    return isNotFirefox
}
function key_log() {
    if (checkMainInput()) {
        if (checkSolver()) {
            numPressed("log(")
        } else {
            DoMath("Log", 1)
        }
    }
    return isNotFirefox
}
function key_invt() {
    if (checkMainInput()) {
        if (checkSolver()) {
            if ((JGetId("ecalc_stack0").value == "") && (activeInput != "cellInputBox")) {
                numPressed("Ans^-1")
            } else {
                numPressed("^-1")
            }
        } else {
            DoMath("Inv", 1)
        }
    }
    return isNotFirefox
}
function key_plus() {
    if (checkMainInput()) {
        if (checkSolver()) {
            if ((JGetId("ecalc_stack0").value == "") && (activeInput != "cellInputBox")) {
                numPressed("Ans+")
            } else {
                numPressed("+")
            }
        } else {
            DoMath("+", 2)
        }
    }
    return isNotFirefox
}
function key_minus() {
    if (checkMainInput()) {
        if (checkSolver()) {
            if ((JGetId("ecalc_stack0").value == "") && (activeInput != "cellInputBox")) {
                numPressed("Ans-")
            } else {
                numPressed("-")
            }
        } else {
            DoMath("-", 2)
        }
    }
    return isNotFirefox
}
function key_times() {
    if (checkMainInput()) {
        if (checkSolver()) {
            if ((JGetId("ecalc_stack0").value == "") && (activeInput != "cellInputBox")) {
                numPressed("Ans*")
            } else {
                numPressed("*")
            }
        } else {
            DoMath("*", 2)
        }
    }
    return isNotFirefox
}
function key_divide() {
    if (checkMainInput()) {
        if (checkSolver()) {
            if ((JGetId("ecalc_stack0").value == "") && (activeInput != "cellInputBox")) {
                numPressed("Ans/")
            } else {
                numPressed("/")
            }
        } else {
            DoMath("/", 2)
        }
    }
    return isNotFirefox
}
function key_pm() {
    if (checkMainInput()) {
        if (SModeValue == 1) {
            numPressed("-")
        } else {
            DoChangeSign()
        }
    }
    return isNotFirefox
}
function key_back() {
    backAtCursor();
    return isNotFirefox
}
function key_clear() {
    clearStack();
    return isNotFirefox
}
function key_clearAll() {
    AllClear();
    return isNotFirefox
}
function key_enter() {
    minusAnsClear = false;
    var g = new RegExp("unitInput");
    if (checkHref == true) {
        if (activeInput == "cellInputBox") {
            moveSolverBox()
        } else {
            if (g.test(activeInput) == true) {
                activeInput = "ecalc_stack0";
                showCursor();
                setCaretToEnd()
            } else {
                if (checkMainInput()) {
                    var h = DoMathBTS(Infix2RPN(JGetId("ecalc_stack0").value));
                    if (h === false) {
                        return false
                    }
                    recallIndex = -1;
                    DoMath("", 0, h)
                }
            }
        }
    }
    return isNotFirefox
}
function stackScrollUp() {
    return setInterval("if((lpos>0)&&(lpos<=lmax)){lpos--};UpdateDisp(lpos)", 150)
}
function stackScrollDown() {
    return setInterval("if((lpos>=0)&&(lpos<lmax)){lpos++};UpdateDisp(lpos)", 150)
}
function stackUp() {
    if ((lpos > 0) && (lpos <= lmax)) {
        lpos--
    }
    UpdateDisp(lpos)
}
function stackDown() {
    if ((lpos >= 0) && (lpos < lmax)) {
        lpos++
    }
    UpdateDisp(lpos)
}
var color1_1 = "#71c938";
var color1_2 = "#c6e9af";
var color1_3 = "#def3d0";
var color1_4 = "#F0FAEA";
var color2_1 = "#0167ff";
var color2_2 = "#aaccff";
var color2_3 = "#c5ddff";
var color2_4 = "#e5f0ff";
var color3_1 = "#fecb00";
var color3_2 = "#ffeeaa";
var color3_3 = "#fff4c5";
var color3_4 = "#fffbe5";
var color4_1 = "#de87cd";
var color4_2 = "#f4d7ee";
var color4_3 = "#faeaf7";
var color4_4 = "#fdf5fb";
var color5_1 = "#515151";
var color5_2 = "#b3b3b3";
var color5_3 = "#e1e1e1";
var color5_4 = "#f2f2f2";
var color6_1 = "#71c938";
var color6_2 = "#aaccff";
var color6_3 = "#c3dbff";
var color6_4 = "#e5f0ff";
var logicState = "true";
var dfState = "false";
var themeState = false;
var menuState = "true";
var paletteState = "units";
var SAngleValue = 1;
var SCoordValue = 1;
var SFormatValue = 1;
var SModeValue = 1;
var solverSize;
var solverType = "";
var cellVisitedFlag = 0;
var cellCurrent = "a0";
var logicBits = new Array();
var logicBinary = "";
var solverArrayA = new Array();
var solverArrayB = new Array();
var solverArrayA2 = new Array();
var solverArrayB2 = new Array();
var solverArrayRoot = new Array();
var greenLight = "#C6E9AF";
var greenDark = "#AADE87";
var greenState = greenDark;
var blueLight = "#AACCFF";
var blueDark = "#80B3FF";
var blueLight = "#AACCFF";
var blueDark = "#80B3FF";
var blueState = blueDark;
var constState = 0;
var varArray = new Array();
var palSlide = 1;
var dStack = 0;
var trialPeriod = 1;
var hoverColor = "#c8c4b7";
var tableRowColorEven = "#E6EAE9";
var tableRowColorOdd = "#FFFFFF";
var cellColorDefault = "#E0E0E0";
var cellColorHot = "#c8c4b7";
var cellColorHover = "#c8c4b7";
var color1 = "";
var color2 = "";
var color3 = "";
var color4 = "";
var unitIndex = -1;
var activeInput = "";
var fromNumPress = false;
var unitCat = 0;
var minusAnsClear = false;
var calcPrec = "3";
var versionNum = "1.5.2";
var mio = false;
var dashboard = false;
var browserMode = false;
var checkHref = true;
var dFloat = 0;
var trackURL = "";
var versionCheckURL = "";
var compMode = "webV";
switch (compMode) {
case "mioT":
    mio = true;
    dashboard = false;
    browserMode = false;
    checkHref = true;
    colorStyle = "2";
    dFloat = 4;
    trackURL = "http://c.statcounter.com/4442100/0/77e79a3b/1/";
    versionCheckURL = "http://www.ecalc.com/files/version_info/ecalc_scientific/mio_trial.txt";
    break;
case "mioP":
    mio = false;
    dashboard = true;
    browserMode = false;
    checkHref = true;
    colorStyle = "6";
    dFloat = 3;
    trackURL = "http://c.statcounter.com/4442177/0/5d086f2e/1/";
    versionCheckURL = "http://www.ecalc.com/files/version_info/ecalc_scientific/mio_purchase.txt";
    break;
case "dashT":
    mio = false;
    dashboard = true;
    browserMode = false;
    checkHref = true;
    colorStyle = "2";
    dFloat = 4;
    trackURL = "http://c.statcounter.com/4442185/0/36000dfb/1/";
    versionCheckURL = "http://www.ecalc.com/files/version_info/ecalc_scientific/mac_trial.txt";
    break;
case "dashP":
    mio = false;
    dashboard = true;
    browserMode = false;
    checkHref = true;
    colorStyle = "2";
    dFloat = 3;
    trackURL = "http://c.statcounter.com/4442220/0/78f1aae6/1/";
    versionCheckURL = "http://www.ecalc.com/files/version_info/ecalc_scientific/mac_purchase.txt";
    break;
case "webV":
    mio = false;
    dashboard = false;
    browserMode = true;
    checkHref = true;
    colorStyle = "2";
    dFloat = 3;
    trackURL = "http://c.statcounter.com/4442220/0/78f1aae6/1/";
    versionCheckURL = "";
    color4 = eval("color6_4");
    color3 = eval("color6_3");
    color2 = eval("color6_2");
    color1 = eval("color6_1");
    break
}
var constArray = new Array();
constArray[0] = new Array("c", "Speed of Light", "2.99792458e8", "m/s");
constArray[1] = new Array("Cc", "Coulomb Constant", "8.9875517873682e9", "Nm&sup2;/coul&sup2;");
constArray[2] = new Array("g", "Acceleration of gravity", "9.80665", "m/s&sup2;");
constArray[3] = new Array("G", "Gravitational Constant", "6.67259e-11", "m&sup3;/kg/s&sup2;");
constArray[4] = new Array("h", "Plancks Constant", "6.6260755e-34", "Js");
constArray[5] = new Array("k", "Boltzmanns Constant", "1.380658e-23", "J/K");
constArray[6] = new Array("F", "Faraday Constant", "9.64853383e4", 'Cmol<sup class="constSuper">&nbsp;-1</sup>');
constArray[7] = new Array("Me", "Electron Rest Mass", "9.1093897e-31", "kg");
constArray[8] = new Array("Mn", "Neutron Rest Mass", "1.6749286e-27", "kg");
constArray[9] = new Array("Mp", "Protron Rest Mass", "1.6726231e-27", "kg");
constArray[10] = new Array("Na", "Avogadros Number", "6.02214179e23", 'mol<sup class="constSuper">&nbsp;-1</sup>');
constArray[11] = new Array("q", "Electron Charge", "1.60217733e-19", "coul");
constArray[12] = new Array("Rb", "Bohr Radius", "5.29177249e-11", "m");
constArray[13] = new Array("Rc", "Molar Gas Constant", "8.31451", "J/mol/K");
constArray[14] = new Array("Rdb", "Rydberg constant", "1.097373153413e7", 'm<sup class="constSuper">&nbsp;-1</sup>');
constArray[15] = new Array("Vm", "Molar Volume", "2.241409e-2", "m&sup3;/mol");
constArray[16] = new Array("&epsilon;0", "Permittivity of a Vacuum", "8.8541878176204e-12", "F/m");
constArray[17] = new Array("&sigma;", "Stefan-Boltzmann Constant", "5.6705119e-8", "W/m&sup2;/K^4");
constArray[18] = new Array("&phi;", "Magnetic Flux Quantum", "2.0678346161e-15", "Wb");
constArray[19] = new Array("&mu;o", "Permeability of a Vacuum", "1.2566370614359e-6", "N/A&sup2;");
constArray[20] = new Array("&mu;b", "Bohr Magneton", "9.2740154e-24", "m&sup2;/Wb");
constArray[21] = new Array("Kj", "Josephson Constant", "4.835979e14", "Hz/V");
constArray[22] = new Array("Zo", "Impedance of Vacuum", "3.76730313461e2", "&#8486;");
constArray[23] = new Array("Go", "Conductance Quantum", "7.7480917004e-5", "S");
var mass = new Array();
mass[0] = new Array("&mu;g", "", "Microgram", 1e-9);
mass[1] = new Array("mg", "", "Milligram", 0.000001);
mass[2] = new Array("gr", "", "Grain", 0.00006479891);
mass[3] = new Array("ct", "", "Carat", 0.0002);
mass[4] = new Array("g", "", "Gram", 0.001);
mass[5] = new Array("oz", "", "Ounce", 0.028349523125);
mass[7] = new Array("lb", "Pound", "(ENG/US)", 0.45359237);
mass[6] = new Array("kg", "", "Kilogram", 1);
mass[8] = new Array("slug", "", "Slug", 14.5939029372);
mass[9] = new Array("ton", "", "Ton", 907.18474);
mass[10] = new Array("tonne", "", "Metric Ton", 1000);
mass[11] = new Array("tonUK", "", "Long Ton", 1016.0469088);
var speed = new Array();
speed[1] = new Array("in/s", "Inches", "Per Second", 0.0254);
speed[2] = new Array("km/h", "Kilometers", "Per Hour", 0.2777777777777777);
speed[3] = new Array("ft/s", "Feet", "Per Second", 0.3048);
speed[4] = new Array("mi/h", "Miles", "Per Hour", 0.44704);
speed[5] = new Array("kn", "", "Knot", 0.5144444444444444);
speed[6] = new Array("m/s", "Meters", "Per Second", 1);
speed[7] = new Array("mach", "", "Mach(SI)", 340.3);
speed[8] = new Array("km/s", "Kilometers", "Per Second", 1000);
speed[9] = new Array("c", "Speed of", "Light", 299792458);
var time = new Array();
time[0] = new Array("fs", "", "Femtosecond", 1e-15);
time[1] = new Array("ps", "", "Picosecond", 1e-12);
time[2] = new Array("ns", "", "Nanosecond", 1e-9);
time[3] = new Array("&mu;s", "", "Microsecond", 0.000001);
time[4] = new Array("ms", "", "Millisecond", 0.001);
time[5] = new Array("s", "", "Second", 1);
time[6] = new Array("min", "", "Minute", 60);
time[7] = new Array("h", "", "Hour", 3600);
time[8] = new Array("d", "", "Day", 86400);
time[9] = new Array("week", "", "Week", 604800);
time[10] = new Array("mo", "Month", "(30 Days)", 2592000);
time[11] = new Array("yr", "", "Year", 31556925.9747);
var power = new Array();
power[3] = new Array("mW", "", "Milliwatt", 0.001);
power[4] = new Array("W", "", "Watt", 1);
power[5] = new Array("hp", "", "Horsepower", 745.699871582);
power[6] = new Array("kW", "", "Kilowatt", 1000);
power[7] = new Array("MW", "", "Megawatt", 1000000);
var volume = new Array();
volume[0] = new Array("ml", "", "Milliliter", 0.000001);
volume[1] = new Array("tsp", "", "Teaspoon", 0.00000492892159375);
volume[2] = new Array("tbsp", "", "Tablespoon", 0.0000147867647813);
volume[3] = new Array("fl oz", "Fluid", "Ounce", 0.0000295735295625);
volume[4] = new Array("c", "", "Cup", 0.0002365882365);
volume[5] = new Array("pt", "", "Pint", 0.000473176473);
volume[6] = new Array("qt", "", "Quart", 0.000946352946);
volume[7] = new Array("l", "", "Liter", 0.001);
volume[8] = new Array("gal", "US", "Gallon", 0.003785411784);
volume[9] = new Array("gal", "British", "Gallon", 0.004546092);
volume[10] = new Array("ft<sup>3</sup>", "", "Cubic Feet", 0.028316846592);
volume[11] = new Array("m<sup>3</sup>", "", "Cubic Meter", 1);
var area = new Array();
area[1] = new Array("mm<sup>2</sup>", "", "Millimeters<sup>2</sup>", 0.000001);
area[2] = new Array("cm<sup>2</sup>", "", "Centimeters<sup>2</sup>", 0.0001);
area[3] = new Array("in<sup>2</sup>", "", "Inches<sup>2</sup>", 0.00064516);
area[4] = new Array("ft<sup>2</sup>", "", "Feet<sup>2</sup>", 0.09290304);
area[5] = new Array("yd<sup>2</sup>", "", "Yards<sup>2</sup>", 0.83612736);
area[6] = new Array("m<sup>2</sup>", "", "Meters<sup>2</sup>", 1);
area[7] = new Array("ac", "", "Acre", 4046.87260987);
area[8] = new Array("ha", "", "Hectare", 10000);
area[9] = new Array("mi<sup>2</sup>", "", "Miles<sup>2</sup>", 2589988.11034);
area[10] = new Array("km<sup>2</sup>", "", "Kilometers<sup>2</sup>", 1000000);
var length = new Array();
length[0] = new Array("&aring;", "", "Angstrom", 1e-10);
length[1] = new Array("mm", "", "Millimeter", 0.001);
length[2] = new Array("cm", "", "Centimeter", 0.01);
length[3] = new Array("in", "", "Inch", 0.0254);
length[4] = new Array("ft", "", "Foot", 0.3048);
length[5] = new Array("yd", "", "Yard", 0.9144);
length[6] = new Array("m", "", "Meter", 1);
length[7] = new Array("km", "", "Kilometer", 1000);
length[8] = new Array("mi", "", "Mile", 1609.344);
length[9] = new Array("nmi", "Nautical", "Mile", 1852);
length[10] = new Array("AU", "Astronomical", "Unit", 149597900000);
length[11] = new Array("ly", "", "Light-Year", 9460528404880000);
var energy = new Array();
energy[1] = new Array("eV", "Electron", "Volt", 1.60217733e-19);
energy[2] = new Array("erg", "", "Erg", 1e-7);
energy[3] = new Array("J", "", "Joule", 1);
energy[4] = new Array("ft&middot;lb", "", "Foot Pound", 1.35581794833);
energy[5] = new Array("cal", "Small", "Calorie", 4.184);
energy[6] = new Array("kcal", "", "Kilocalorie", 4184);
energy[7] = new Array("btu", "British", "Thermal Unit", 1055.05585262);
energy[8] = new Array("kWh", "Kilowatt", "Hour", 3600000);
energy[9] = new Array("therm", "", "Therm", 105506000);
var temp = new Array();
temp[4] = new Array("&deg;C", "", "Celsius", "C");
temp[5] = new Array("&deg;F", "", "Fahrenheit", "F");
temp[6] = new Array("&nbsp;K", "", "Kelvin", "K");
temp[7] = new Array("&deg;R", "", "Rankine", "R");
var force = new Array();
force[0] = new Array("dyn", "", "Dyne", 0.00001);
force[1] = new Array("gf", "", "Gram-Force", 0.00980665);
force[2] = new Array("pdl", "", "Poundal", 0.138254954376);
force[3] = new Array("N", "", "Newton", 1);
force[4] = new Array("lbf", "", "Pound Force", 4.4482216152605);
force[5] = new Array("kgf", "Kilogram", "Force", 9.80665);
force[6] = new Array("kp", "", "Kilopond", 9.80665);
force[7] = new Array("kN", "", "Kilonewton", 1000);
force[8] = new Array("kip", "", "Kip", 4448.2216152605);
force[9] = new Array("tonf", "Ton-Force", "Short (US)", 8896.443230521);
force[10] = new Array("tonf", "Ton-Force", "Metric", 9806.65);
force[11] = new Array("tonf", "Ton-Force", "Long (UK)", 9964.01641818352);
var press = new Array();
press[1] = new Array("Pa", "", "Pascal", 1);
press[2] = new Array("mmHg", "Millimeters", "of Mercury", 133.322387415);
press[3] = new Array("torr", "", "Torr", 133.322368421);
press[4] = new Array("inH20", "Inches", "of Water", 248.84);
press[5] = new Array("kPa", "", "KiloPascal", 1000);
press[6] = new Array("inHg", "Inches", "of Mercury", 3386.388640341);
press[7] = new Array("psi", "Pound Per", "Square Inch", 6894.75729317);
press[8] = new Array("bar", "", "Bar", 100000);
press[9] = new Array("atm", "", "Atmosphere", 101325);
var light = new Array();
light[2] = new Array("lx", "", "Lux", 1);
light[3] = new Array("lm/m<sup>2</sup>", "Lumen Per", "Square m", 1);
light[4] = new Array("fc", "Foot", "Candle", 10.7639104167);
light[5] = new Array("lm/ft<sup>2</sup>", "Lumen Per", "Square ft", 10.7639104167);
light[6] = new Array("lm/in<sup>2</sup>", "Lumen Per", "Square in", 1550.0031);
light[7] = new Array("lm/cm<sup>2</sup>", "Lumen Per", "Square cm", 10000);
light[8] = new Array("ph", "", "Phot", 10000);
light[9] = new Array("w/cm<sup>2</sup>", "Watt Per", "Square cm", 6830000);
var unitType = new Array(length, area, volume, speed, force, mass, time, power, energy, temp, light, press);
var unitTitleText = new Array("Length", "Area", "Volume", "Speed", "Force", "Mass", "Time", "Power", "Energy", "Temperature", "Light&nbsp;(Illuminance)", "Pressure");
var unitStates = new Array(12);
for (i = 0; i < unitStates.length; i++) {
    unitStates[i] = -1
}
var unitStore = new Array(12);
for (i = 0; i < unitStore.length; i++) {
    unitStore[i] = new Array(12)
}
var unitDisp = new Array(12);
for (i = 0; i < unitDisp.length; i++) {
    unitDisp[i] = new Array(12)
}
var ids = new Array("units", "const", "solver", "logic", "help", "fract");

function tempConvt(l, h, g) {
    if (h == "C") {
        l = l + 273.15
    } else {
        if (h == "F") {
            l = ((l + 459.67) * 5) / 9
        } else {
            if (h == "R") {
                l = (l * 5) / 9
            }
        }
    }
    if (l < 0) {
        return "Not Valid"
    }
    if (g == "C") {
        l = l - 273.15
    } else {
        if (g == "F") {
            l = ((l * 9) / 5) - 459.67
        } else {
            if (g == "R") {
                l = (l * 9) / 5
            }
        }
    }
    return l
}
var unitListHovers = new Array();
for (i = 0; i <= 11; i++) {
    unitListHovers[i] = new Array(-1, -1)
}
var unitInputArray = new Array();
for (i = 0; i <= 11; i++) {
    unitInputArray[i] = 1
}
function switchid(h) {
    if ((h != "fract") && (h != 0)) {
        paletteState = h;
        document.getElementById("df_gui").className = "df_button";
        dfState = "false"
    }
    for (var g = 0; g < ids.length; g++) {
        document.getElementById(ids[g]).style.display = "none"
    }
    if (h != 0) {
        document.getElementById(h).style.display = "block";
        document.getElementById("pButtons").className = h + "Button buttons"
    }
}
function toggle_df() {
    if ((dStack < 2) || (dFloat == 3)) {
        if (dfState == "false") {
            document.getElementById("df_gui").className = "df_button_hot";
            if (dashboard) {
                if (palSlide != 1) {
                    slideOpen()
                }
            } else {
                if (mio) {
                    if (palSlide != 1) {
                        openPal()
                    }
                } else {
                    if (palSlide != 1) {
                        openPalAnim()
                    }
                }
            }
            setTimeout("switchid('fract')", 50);
            dfState = "true"
        } else {
            document.getElementById("df_gui").className = "df_button";
            switchid(paletteState);
            dfState = "false"
        }
    }
    if (window.widget) {
        savePrefs()
    }
}
function menuCycle() {
    if ((dStack < 2) || (dFloat == 3)) {
        if (menuState == "true") {
            activeInput = "";
            document.getElementById("ecalc_screen").style.display = "none";
            document.getElementById("ecalc_menu").style.display = "block";
            document.getElementById("menu_gui").className = "menu_button";
            updatePrecision();
            menuState = "false"
        } else {
            calcPrec = document.getElementById("ecalc_precision").value;
            updatePrecision();
            document.getElementById("ecalc_screen").style.display = "block";
            document.getElementById("ecalc_menu").style.display = "none";
            document.getElementById("menu_gui").className = "menu_button_hot";
            switchid(paletteState);
            menuState = "true";
            activeInput = "ecalc_stack0";
            showCursor();
            setCaretToEnd()
        }
    }
}
function changeBase(g) {
    if (logicState == "true") {
        document.getElementById("bases").className = g + " bases_1";
        switch (g) {
        case "binMode":
            document.getElementById("hexButtons").className = "hexDisabled";
            document.getElementById("numbers89").className = "digits89Disabled";
            document.getElementById("numbers27").className = "digits27Disabled";
            break;
        case "octMode":
            document.getElementById("hexButtons").className = "hexDisabled";
            document.getElementById("numbers89").className = "digits89Disabled";
            document.getElementById("numbers27").className = "";
            break;
        case "decMode":
            document.getElementById("hexButtons").className = "hexDisabled";
            document.getElementById("numbers89").className = "";
            document.getElementById("numbers27").className = "";
            break;
        case "hexMode":
            document.getElementById("hexButtons").className = "hexButtons_1";
            document.getElementById("numbers89").className = "";
            document.getElementById("numbers27").className = "";
            break
        }
    }
}
function SAngleDisp() {
    document.getElementById("menu_rad").className = "menu_radA";
    document.getElementById("menu_deg").className = "menu_degA";
    document.getElementById("menu_grad").className = "menu_gradA";
    switch (SAngleValue) {
    case 1:
        document.getElementById("status_angle").className = "status_rad";
        document.getElementById("menu_rad").className = "menu_radB";
        AngUnit = "R";
        break;
    case 2:
        document.getElementById("status_angle").className = "status_deg";
        document.getElementById("menu_deg").className = "menu_degB";
        AngUnit = "D";
        break;
    case 3:
        document.getElementById("status_angle").className = "status_grd";
        document.getElementById("menu_grad").className = "menu_gradB";
        AngUnit = "G";
        break
    }
    if (window.widget) {
        savePrefs()
    }
    UpdateDisp(lpos)
}
function SCoordDisp() {
    document.getElementById("menu_rec").className = "menu_recA";
    document.getElementById("menu_polar").className = "menu_polarA";
    switch (SCoordValue) {
    case 1:
        document.getElementById("status_coord").className = "status_rec";
        document.getElementById("menu_rec").className = "menu_recB";
        break;
    case 2:
        document.getElementById("status_coord").className = "status_pol";
        document.getElementById("menu_polar").className = "menu_polarB";
        break
    }
    if (window.widget) {
        savePrefs()
    }
    UpdateDisp(lpos)
}
function SModeDisp() {
    document.getElementById("menu_rpn").className = "menu_rpnA";
    document.getElementById("menu_alg").className = "menu_algA";
    switch (SModeValue) {
    case 1:
        document.getElementById("status_mode").className = "status_alg";
        document.getElementById("enter_gui").className = "alg_entr";
        document.getElementById("menu_alg").className = "menu_algB";
        document.getElementById("ans_gui").className = "ans_button";
        document.getElementById("comma_gui").className = "i_button";
        document.getElementById("ecalc_stack1").className = "stackInfix1";
        document.getElementById("ecalc_stack2").className = "stackInfix2";
        document.getElementById("ecalc_stack3").className = "stackInfix3";
        document.getElementById("ecalc_stack4").className = "stackInfix4";
        document.getElementById("ecalc_hovr1").className = "hovrRPN1";
        document.getElementById("ecalc_hovr2").className = "hovrRPN2";
        document.getElementById("ecalc_hovr3").className = "hovrRPN3";
        document.getElementById("ecalc_hovr4").className = "hovrRPN4";
        break;
    case 2:
        document.getElementById("status_mode").className = "status_rpn";
        document.getElementById("enter_gui").className = "rpn_entr";
        document.getElementById("menu_rpn").className = "menu_rpnB";
        document.getElementById("ans_gui").className = "swap_button";
        document.getElementById("comma_gui").className = "comma_button";
        document.getElementById("ecalc_stack1").className = "stackRPN1";
        document.getElementById("ecalc_stack2").className = "stackRPN2";
        document.getElementById("ecalc_stack3").className = "stackRPN3";
        document.getElementById("ecalc_stack4").className = "stackRPN4";
        document.getElementById("ecalc_hovr1").className = "hovrRPN1";
        document.getElementById("ecalc_hovr2").className = "hovrRPN2";
        document.getElementById("ecalc_hovr3").className = "hovrRPN3";
        document.getElementById("ecalc_hovr4").className = "hovrRPN4";
        break
    }
    if (window.widget) {
        savePrefs()
    }
    UpdateDisp(lpos)
}
function SFormatDisp() {
    document.getElementById("menu_std").className = "menu_stdA";
    document.getElementById("menu_fix").className = "menu_fixA";
    document.getElementById("menu_sci").className = "menu_sciA";
    document.getElementById("menu_eng").className = "menu_engA";
    switch (SFormatValue) {
    case 1:
        document.getElementById("status_format").className = "status_std";
        document.getElementById("menu_std").className = "menu_stdB";
        break;
    case 2:
        document.getElementById("status_format").className = "status_fix";
        document.getElementById("menu_fix").className = "menu_fixB";
        break;
    case 3:
        document.getElementById("status_format").className = "status_sci";
        document.getElementById("menu_sci").className = "menu_sciB";
        break;
    case 4:
        document.getElementById("status_format").className = "status_eng";
        document.getElementById("menu_eng").className = "menu_engB";
        break
    }
    if (window.widget) {
        savePrefs()
    }
    UpdateDisp(lpos)
}
function menuAngle(g) {
    SAngleValue = g;
    SAngleDisp()
}
function menuFormat(g) {
    SFormatValue = g;
    SFormatDisp()
}
function menuCoord(g) {
    SCoordValue = g;
    SCoordDisp()
}
function menuMode(g) {
    SModeValue = g;
    SModeDisp()
}
function incrtAngle() {
    if (SAngleValue == 3) {
        SAngleValue = 1
    } else {
        SAngleValue += 1
    }
    SAngleDisp()
}
function incrtCoord() {
    if (SCoordValue == 2) {
        SCoordValue = 1
    } else {
        SCoordValue = 2
    }
    SCoordDisp()
}
function incrtMode() {
    if (SModeValue == 2) {
        SModeValue = 1
    } else {
        SModeValue = 2
    }
    SModeDisp()
}
function incrtFormat() {
    if (SFormatValue == 4) {
        SFormatValue = 1
    } else {
        SFormatValue += 1
    }
    SFormatDisp()
}
function OpenWindow() {
    var g = window.open("index2.html", "NewWindow", "width=800,height=600,scrollbars=no")
}
function flipBits(h) {
    var g = base(h, "01");
    g = g + "";
    g = g.split("");
    for (i = 31; i >= 0; i--) {
        if (i > g.length - 1) {
            document.getElementById("bit" + i.toString()).className = "logic_zero";
            logicBits[i] = "0"
        } else {
            j = g.length - i - 1;
            if (g[i] == "1") {
                document.getElementById("bit" + j.toString()).className = "logic_one";
                logicBits[j] = "1"
            } else {
                document.getElementById("bit" + j.toString()).className = "logic_zero";
                logicBits[j] = "0"
            }
        }
    }
}
function bitFlip(g) {
    var h;
    logicBits[g] ^= 1;
    logicBinary = "";
    for (i = 31; i >= 0; i--) {
        if (logicBits[i] == 1) {
            logicBinary += "1";
            document.getElementById("bit" + i.toString()).className = "logic_one"
        } else {
            if (logicBits[i] == 0) {
                logicBinary += "0";
                document.getElementById("bit" + i.toString()).className = "logic_zero"
            }
        }
    }
    h = unbase(logicBinary, "01");
    document.getElementById("dec_box").value = h;
    if (h == 0) {
        document.getElementById("hex_box").value = 0;
        document.getElementById("oct_box").value = 0
    } else {
        document.getElementById("hex_box").value = base(h, "0123456789ABCDEF");
        document.getElementById("oct_box").value = base(h, "01234567")
    }
}
function bitInit() {
    var g = "";
    for (i = 0; i <= 31; i++) {
        logicBits[i] = 0;
        g = "bit" + i.toString();
        document.getElementById(g).className = "logic_zero"
    }
    document.getElementById("hex_box").value = "";
    document.getElementById("oct_box").value = "";
    document.getElementById("dec_box").value = ""
}
function UnitInit(g) {
    var h = "";
    var m = "";
    var l = "";
    for (i = 0; i <= 10; i++) {
        l = i.toString();
        h = "UC" + l;
        m = "ucInit" + l;
        document.getElementById(h).className = m
    }
    h = g.toString();
    document.getElementById(("UC" + h)).className = ("ucStick" + h)
}
function updateBases(g) {
    if (g == "dec_box") {
        decValue = document.getElementById("dec_box").value;
        if (decValue >= 0) {
            document.getElementById("hex_box").value = base(decValue, "0123456789ABCDEF");
            document.getElementById("oct_box").value = base(decValue, "01234567");
            flipBits(decValue)
        }
    } else {
        if (g == "oct_box") {
            octValue = document.getElementById("oct_box").value;
            decValue = unbase(octValue, "01234567");
            if (decValue >= 0) {
                document.getElementById("hex_box").value = base(decValue, "0123456789ABCDEF");
                document.getElementById("dec_box").value = decValue;
                flipBits(decValue)
            }
        } else {
            if (g == "hex_box") {
                hexValue = (document.getElementById("hex_box").value).toUpperCase();
                decValue = unbase(hexValue, "0123456789ABCDEF");
                if (decValue >= 0) {
                    document.getElementById("oct_box").value = base(decValue, "01234567");
                    document.getElementById("dec_box").value = decValue;
                    flipBits(decValue)
                }
            }
        }
    }
}
function clearBases() {
    document.getElementById("dec_box").value = "";
    document.getElementById("hex_box").value = "";
    document.getElementById("oct_box").value = "";
    flipBits(0)
}
function base(m, l) {
    var g = l.length;
    var h = "";
    while (m > 0) {
        h = l.charAt(m % g) + h;
        m = Math.floor(m / g)
    }
    return h
}
function unbase(m, o) {
    var h = o.length;
    var l = 0;
    for (var g = 1; m.length > 0; g *= h) {
        l += o.indexOf(m.charAt(m.length - 1)) * g;
        m = m.substr(0, m.length - 1)
    }
    return l
}
function constLoad() {
    if (constState == 0) {
        for (var g = 0; g <= 11; g++) {
            document.getElementById("CNSTAbv" + g.toString()).innerHTML = constArray[g][0];
            document.getElementById("CNSTDesc" + g.toString()).innerHTML = constArray[g][1];
            document.getElementById("CNSTDesc" + g.toString()).style.width = "220px"
        }
        document.getElementById("CNSTDesc12").innerHTML = "More"
    } else {
        if (constState == 1) {
            for (g = 0; g <= 11; g++) {
                document.getElementById("CNSTAbv" + g.toString()).innerHTML = constArray[g + 12][0];
                document.getElementById("CNSTDesc" + g.toString()).innerHTML = constArray[g + 12][1];
                document.getElementById("CNSTDesc" + g.toString()).style.width = "220px"
            }
            document.getElementById("CNSTDesc12").innerHTML = "Back"
        }
    }
}
function constMore() {
    constState ^= 1;
    constLoad()
}
function constDisp(h) {
    if (h != 12) {
        var g = constArray[(h + constState * 12)][2];
        g = parseFloat(g);
        if ((g > 10) || (g < 1)) {
            document.getElementById("constValueHover").innerHTML = g.toExponential(4)
        } else {
            document.getElementById("constValueHover").innerHTML = g.toFixed(4)
        }
        document.getElementById("constUnitsHover").innerHTML = "&nbsp;" + constArray[(h + constState * 12)][3]
    }
    document.getElementById("tile" + h).style.backgroundColor = color2
}
function constClear(g) {
    document.getElementById("constValueHover").innerHTML = "";
    document.getElementById("constUnitsHover").innerHTML = "";
    document.getElementById("tile" + g).style.backgroundColor = "transparent"
}
function constPush(g) {
    numPressed(constArray[(g + constState * 12)][2])
}
var recallIndex = -1;

function recallExp(h) {
    var g = "";
    if ((h == "down") && (recallIndex > 0)) {
        recallIndex--
    } else {
        if ((h == "up") && (recallIndex < lc)) {
            recallIndex++
        }
    }
    if (SModeValue == 1) {
        g = expList[lc - recallIndex]
    } else {
        if (SModeValue == 2) {
            var l = cNum(0, 0);
            l = stk[lc - recallIndex];
            if (SCoordValue == 1) {
                g = cShowPair(l, 15)
            } else {
                if (SCoordValue == 2) {
                    if (l.i == 0) {
                        g = cShowPair(l, 15)
                    } else {
                        g = cShowPolar(l, 15)
                    }
                }
            }
        }
    }
    document.getElementById("ecalc_stack0").value = "";
    numPressed(g);
    showCursor()
}
function handleEnterKey(h) {
    h = h || window.event;
    var g;
    if (h.keyCode) {
        g = h.keyCode
    } else {
        if (h.which) {
            g = h.which
        }
    }
    if (g == 13) {
        key_enter();
        return false
    } else {
        if (g == 46) {
            if (JGetId("ecalc_stack0").value == "") {
                key_clear();
                return false
            }
        } else {
            if (g == 38) {
                recallExp("up");
                return false
            } else {
                if (g == 40) {
                    recallExp("down");
                    return false
                } else {
                    if (g == 8) {
                        if (JGetId("ecalc_stack0").value == "") {
                            minusAnsClear = true
                        }
                    }
                }
            }
        }
    }
}
function handleStackEntry(l) {
    l = l || window.event;
    var h;
    if (l.keyCode) {
        h = l.keyCode
    } else {
        if (l.which) {
            h = l.which
        }
    }
    var g = String.fromCharCode(h);
    if ((g == "m") && (SModeValue == 2)) {
        DoChangeSign();
        return false
    }
    if (g == "@") {
        document.getElementById("ecalc_stack0").value += "\u2220";
        return false
    }
    if ((g == "+") || (g == "/") || (g == "*") || (g == "-") || (g == "^")) {
        if (SModeValue == 1) {
            if ((JGetId("ecalc_stack0").value == "") && (minusAnsClear == false)) {
                numPressed("Ans" + g)
            } else {
                return true
            }
        } else {
            if (g == "+") {
                DoMath("+", 2)
            } else {
                if (g == "-") {
                    DoMath("-", 2)
                } else {
                    if (g == "*") {
                        DoMath("*", 2)
                    } else {
                        if (g == "/") {
                            DoMath("/", 2)
                        } else {
                            if (g == "^") {
                                DoMath("Pow", 2)
                            }
                        }
                    }
                }
            }
        }
        return false
    }
}
function handle_keypress(l) {
    l = l || window.event;
    var h;
    if (l.keyCode) {
        h = l.keyCode
    } else {
        if (l.which) {
            h = l.which
        }
    }
    var g = String.fromCharCode(h);
    var m = /\d/;
    if (g.search(m) != -1) {
        numPressed(g);
        return false
    }
    if (h == 46) {
        Decimal();
        return false
    }
    if ((g == "+") || (g == "/") || (g == "*") || (g == "-")) {
        Operation(g);
        return false
    }
    if (h == 13) {
        Operation("=")
    }
    return true
}
function addLi(m, w, q) {
    var t = "";
    var A = "";
    var l = "";
    var h = "";
    var o = 30;
    var g = 5;
    var u = q * (o + g + 2);
    var v = w * (o + g + 2) + g;
    document.getElementById(m).style.width = u.toString() + "px";
    document.getElementById(m).style.height = v.toString() + "px";
    document.getElementById("solverWrap").style.width = ((w + q) * u + 14).toString() + "px";
    document.getElementById("solverBracketsWrap").style.width = ((w + q) * u + 32).toString() + "px";
    document.getElementById("solverWrap").style.height = (v).toString() + "px";
    document.getElementById("solverBracketsWrap").style.height = (v + 18).toString() + "px";
    for (i = 0; i <= (w * q - 1); i++) {
        var z = document.createElement("LI");
        z.className = "cell";
        l = Math.floor(i / q) + 1;
        h = (i % w) + 1;
        if (q == 1) {
            t = "b" + i.toString();
            A = "b" + l.toString();
            solverArrayB[i] = ""
        } else {
            t = "a" + i.toString();
            A = "a" + l.toString() + h.toString();
            solverArrayA[i] = ""
        }
        z.style.width = o.toString() + "px";
        z.style.height = o.toString() + "px";
        z.style.marginRight = g.toString() + "px";
        z.style.marginTop = g.toString() + "px";
        z.id = t;
        z.onmouseover = hoverCell;
        z.onmouseout = visitedCell;
        z.onclick = clickCell;
        z.appendChild(document.createTextNode(A));
        document.getElementById(m).appendChild(z)
    }
}
function clearList(h) {
    var g;
    g = document.getElementById(h);
    if (g.hasChildNodes()) {
        while (g.childNodes.length >= 1) {
            g.removeChild(g.firstChild)
        }
    }
    return false
}
function buildLinear(g) {
    clearSolver();
    document.getElementById("cellInputBox").value = "";
    solverDisplay("linearWrapper");
    addLi("AMatrix", g, g);
    addLi("BVector", g, 1);
    solverSize = g;
    solverType = "linearWrapper";
    activeCell("a0")
}
function buildSize(g, h) {
    temp = "";
    for (i = 1; i <= 7; i++) {
        temp = h + i.toString();
        if (i <= g) {
            document.getElementById(temp).style.display = "block"
        } else {
            document.getElementById(temp).style.display = "none"
        }
    }
}
function solverDisplay(g) {
    document.getElementById("solverBuild").style.display = "none";
    document.getElementById("linearWrapper").style.display = "none";
    document.getElementById("rootWrapper").style.display = "none";
    document.getElementById("answerWrapper").style.display = "none";
    document.getElementById(g).style.display = "block"
}
function buildRoot(g) {
    clearRoots();
    solverDisplay("rootWrapper");
    setTimeout(function() {
        buildSize(g, "roota")
    }, 50);
    solverType = "rootWrapper";
    solverSize = g - 1
}
function exitSolver() {
    activeInput = "ecalc_stack0";
    showCursor();
    solverDisplay("solverBuild");
    cellCurrent = "a0"
}
function solverAnswers() {
    activeInput = "ecalc_stack0";
    showCursor();
    solverDisplay("answerWrapper");
    buildSize(solverSize, "solverAnswer")
}
function backSolver() {
    solverDisplay(solverType);
    if (solverType == "linearWrapper") {
        document.getElementById("cellInputBox").value = solverArrayA[0]
    }
}
function saveRootValues() {
    for (i = 0; i <= solverSize; i++) {
        solverArrayRoot[i] = document.getElementById("rootCoeff" + i.toString()).value
    }
}
function clearRoots() {
    for (i = 0; i <= 6; i++) {
        solverArrayRoot[i] = "";
        document.getElementById("rootCoeff" + i.toString()).value = ""
    }
}
function clearSolver() {
    clearList("AMatrix");
    clearList("BVector")
}
function hoverCell() {
    if (this.id != cellCurrent) {
        this.style.backgroundColor = cellColorHover
    }
}
function clickCell() {
    activeCell(this.id)
}
function activeCell(m) {
    var o, l;
    document.getElementById(cellCurrent).style.backgroundColor = cellColorDefault;
    document.getElementById(cellCurrent).style.fontWeight = "normal";
    document.getElementById(cellCurrent).style.color = "#999999";
    if (document.getElementById("cellInputBox").value != "") {
        document.getElementById(cellCurrent).style.fontWeight = "bold";
        document.getElementById(cellCurrent).style.color = "#000000"
    }
    document.getElementById(m).style.backgroundColor = cellColorHot;
    document.getElementById(m).style.color = "black";
    document.getElementById(m).style.fontWeight = "bold";
    var h = "";
    h = cellCurrent.replace(/a|b/, "");
    var g = "";
    g = cellCurrent.replace(/\d\d|\d/, "");
    if (g == "a") {
        solverArrayA[parseInt(h)] = document.getElementById("cellInputBox").value
    } else {
        if (g == "b") {
            solverArrayB[parseInt(h)] = document.getElementById("cellInputBox").value
        }
    }
    g = m;
    g = g.replace(/\d\d|\d/, "");
    h = m;
    h = h.replace(/a|b/, "");
    if (g == "a") {
        document.getElementById("cellInputBox").value = solverArrayA[parseInt(h)]
    } else {
        if (g == "b") {
            document.getElementById("cellInputBox").value = solverArrayB[parseInt(h)]
        }
    }
    cellText = document.getElementById(m).innerHTML;
    g = cellText;
    g = g.replace(/\d\d|\d/, "");
    h = cellText;
    h = h.replace(/a|b/, "");
    document.getElementById("cellIDText").innerHTML = g;
    document.getElementById("cellIDTextSub").innerHTML = h;
    activeInput = "cellInputBox";
    showCursor();
    cellCurrent = m
}
function visitedCell() {
    if (this.id != cellCurrent) {
        this.style.backgroundColor = cellColorDefault
    }
}
function handleEnter(g) {
    var h = g.keyCode ? g.keyCode : g.which ? g.which : g.charCode;
    if (h == 13) {
        moveSolverBox()
    }
    return false
}
function moveSolverBox() {
    var h = "";
    var g = "";
    h = cellCurrent.replace(/a|b/, "");
    g = cellCurrent.replace(/\d\d|\d/, "");
    h = parseInt(h);
    if (g == "a") {
        if ((h % solverSize) == (solverSize - 1)) {
            activeCell("b" + (Math.floor(h / solverSize)).toString())
        } else {
            h = h + 1;
            activeCell("a" + h.toString())
        }
    } else {
        if (g == "b") {
            if (h == (solverSize - 1)) {
                activeCell("a0")
            } else {
                activeCell("a" + ((h + 1) * solverSize).toString())
            }
        }
    }
}
function setSelectionRange(h, l, m) {
    if (h.createTextRange) {
        var g = h.createTextRange();
        g.collapse(true);
        g.moveEnd("character", m);
        g.moveStart("character", l);
        g.select()
    } else {
        if (h.setSelectionRange) {
            h.focus();
            h.setSelectionRange(l, m)
        }
    }
}
function calc() {
    var l = document.getElementById("dToFInput");
    var w = document.getElementById("fractions");
    w.innerHTML = "";
    var u = Math.abs(l.value);
    if (isNaN(u)) {
        u = 0
    }
    var h = [0, 1];
    var o = [1, 0];
    var v = getMaxNumerator(u);
    var g = u;
    var m, z = NaN;
    for (var t = 2; t < 1000; t++) {
        var q = Math.round(g);
        h[t] = q * h[t - 1] + h[t - 2];
        o[t] = q * o[t - 1] + o[t - 2];
        m = h[t] / o[t];
        if (m == z || Math.abs(h[t]) > v) {
            break
        }
        w.innerHTML += "<b>" + Math.abs(h[t]) + "</b>/<b>" + Math.abs(o[t]) + "</b> = " + m + "<br>";
        if (m == u) {
            break
        }
        z = m;
        g = 1 / (g - q)
    }
}
function getMaxNumerator(u) {
    var o = null;
    var w = u.toString().indexOf("E");
    if (w == -1) {
        w = u.toString().indexOf("e")
    }
    if (w == -1) {
        o = u.toString()
    } else {
        o = u.toString().substring(0, w)
    }
    var h = null;
    var l = o.toString().indexOf(".");
    if (l == -1) {
        h = o
    } else {
        if (l == 0) {
            h = o.substring(1, o.length)
        } else {
            if (l < o.length) {
                h = o.substring(0, l) + o.substring(l + 1, o.length)
            }
        }
    }
    var z = h;
    var g = z.toString().length;
    var q = u;
    var v = q.toString().length;
    if (q == 0) {
        v = 0
    }
    var m = g - v;
    for (var t = m; t > 0 && z % 2 == 0; t--) {
        z /= 2
    }
    for (var t = m; t > 0 && z % 5 == 0; t--) {
        z /= 5
    }
    return z
}
function myAlert() {
    if (palSlide == 1) {
        document.getElementById("palette_wrap").style.display = "none";
        document.getElementById("palette_wrap").style.display = "block"
    }
}
function insertD() {
    document.getElementById("ecalc_body").innerHTML += "<div id='ecalc_demo'><div id='ecalcBuy' onmouseover='windowButtonHover(this);' onmouseout='windowButtonOut(this);' onmousedown='buyEcalc();'></div><div id='ecalcCont' class='ecalcContOut' onmouseover='windowButtonHover(this);' onmouseout='windowButtonOut(this);' onmousedown='restD();'></div></div>"
}
function insertSplash() {
    if (OSName == "mac") {
        document.getElementById("ecalc_body").innerHTML += "<div id='ecalc_demo'><div id='buyNow' style='position:absolute;top:104px;width:126px;height:30px;left:35px;'><a href='http://www.ecalc.com/buy/mac/' style='display:block;height:30px;cursor:pointer;'></a></div><div id='freeTrial' style='position:absolute;top:104px;width:126px;height:30px;left:173px;'><a href='http://www.ecalc.com/download/'style='display:block;height:30px;cursor:pointer;'></a></div><div id='ecalcCont' class='ecalcContOut' onmouseover='windowButtonHover(this);' onmouseout='windowButtonOut(this);' onmousedown='restD();' style='width:135px;left:200px;'></div></div>"
    } else {
        document.getElementById("ecalc_body").innerHTML += "<div id='ecalc_demo'><div id='buyNow' style='position:absolute;top:104px;width:126px;height:30px;left:35px;'><a href='http://www.ecalc.com/buy/windows/' style='display:block;height:30px;cursor:pointer;'></a></div><div id='freeTrial' style='position:absolute;top:104px;width:126px;height:30px;left:173px;'><a href='http://www.ecalc.com/download/'style='display:block;height:30px;cursor:pointer;'></a></div><div id='ecalcCont' class='ecalcContOut' onmouseover='windowButtonHover(this);' onmouseout='windowButtonOut(this);' onmousedown='restD();' style='width:135px;left:200px;'></div></div>"
    }
}
var req;

function loadXMLDoc(h, g) {
    req = false;
    if (window.XMLHttpRequest && !(window.ActiveXObject)) {
        try {
            req = new XMLHttpRequest()
        } catch (l) {
            req = false
        }
    } else {
        if (window.ActiveXObject) {
            try {
                req = new ActiveXObject("Msxml2.XMLHTTP")
            } catch (l) {
                try {
                    req = new ActiveXObject("Microsoft.XMLHTTP")
                } catch (l) {
                    req = false
                }
            }
        }
    }
    if (req) {
        if (g == "checkVersion") {
            req.onreadystatechange = checkVersion
        }
        req.open("GET", h, true);
        req.send(null)
    }
}
var criticalUpdate = false;

function showCritical() {
    openPal();
    switchid("help")
}
function updatesPage() {
    if (window.widget) {
        widget.openURL("http://www.ecalc.com/download/")
    } else {
        mio_openURL("http://www.ecalc.com/download/")
    }
}
function checkVersion() {
    if (req.readyState == 4) {
        var m = req.responseText;
        var g = versionNum;
        var h = new Array();
        var o = "";
        var l = "";
        h = m.split("_");
        arrayNewVersion = h[0];
        arrayNewMessage = h[1];
        arrayNewVersion = arrayNewVersion.replace(/\./g, "");
        g = g.replace(/\./g, "");
        if (arrayNewVersion > g) {
            if (arrayNewMessage.indexOf("*") != -1) {
                criticalUpdate = true;
                arrayNewMessage = arrayNewMessage.replace(/\*/g, "");
                showCritical()
            }
            document.getElementById("updateText").innerHTML = arrayNewMessage
        }
    }
}
var OSName = "false";

function checkOS() {
    if (navigator.appVersion.indexOf("Win") != -1) {
        OSName = "windows"
    }
    if (navigator.appVersion.indexOf("Mac") != -1) {
        OSName = "mac"
    }
    if (navigator.appVersion.indexOf("X11") != -1) {
        OSName = "unix"
    }
    if (navigator.appVersion.indexOf("Linux") != -1) {
        OSName = "linux"
    }
}
function initGUI() {
    document.getElementById("ecalc_stack0").value = "";
    hookEvent("ecalc_body", "mousewheel", moveObject);
    if (window.widget) {
        getPrefs()
    }
    updatePrecision();
    SAngleDisp();
    SCoordDisp();
    SFormatDisp();
    SModeDisp();
    bitInit();
    changeStyle(colorStyle, 1);
    smallColorInit();
    smallColorBackgroundSet(colorStyle);
    unitConvInit();
    constLoad();
    UpdateDisp(lpos);
    switchid("units");
    if (compMode != "webV") {
        restorePal()
    }
    if (dFloat != 3) {
        insertD();
        restD()
    }
    document.getElementById("infoHeader").innerHTML += "&nbsp;" + versionNum;
    document.getElementById("dToFInput").value = "";
    //document.getElementById("ecalcBody").ondragstart = function() {
    //    return false
    //};
    activeInput = "ecalc_stack0";
    document.getElementById("ecalc_stack0").setAttribute("autocomplete", "off");
    if (mio) {
        mio_autoShape_apply()
    }
    hideFocusBorders();
    AllClear();
    if (browserMode == true) {
        //if ((location.href).indexOf("http://www.eeweb.com/") != -1) {
            checkHref = true
        //} else {
        //    checkHref = false;
        //    document.getElementById("ecalc_wrapW").style.display = "none"
        //}
    }
    if (compMode != "webV") {
        setTimeout(function() {
            loadXMLDoc(trackURL, null)
        }, 500)
    }
}
function numPressed(h) {
    if (checkHref == true) {
        if ((dStack < 2) || (dFloat == 3)) {
            showCursor();
            var g = new RegExp("unitInput");
            if (g.test(activeInput) == true) {
                fromNumPress = true;
                insertAtCursor(h);
                unitsConvert(unitStates[unitCat])
            } else {
                if ((activeInput == "hex_box") || (activeInput == "dec_box") || (activeInput == "oct_box")) {
                    insertAtCursor(h);
                    updateBases(activeInput)
                } else {
                    if (activeInput == "dToFInput") {
                        insertAtCursor(h);
                        calc()
                    } else {
                        if (activeInput != "") {
                            insertAtCursor(h)
                        }
                    }
                }
            }
            if (/Firefox[\/\s](\d+\.\d+)/.test(navigator.userAgent)) {
                return false
            } else {
                return true
            }
        } else {
            return false
        }
    }
}
function clearInputBox() {
    JGetId("ecalc_stack0").value = ""
}
function clearStack() {
    if ((dStack < 2) || (dFloat == 3)) {
        var g = new RegExp("unitInput");
        if (activeInput == "ecalc_stack0") {
            var h = JGetId("ecalc_stack0").value;
            if (h != "") {
                h = JGetId("ecalc_stack0").value = ""
            } else {
                if (lc > -1) {
                    lc--;
                    lpos = lc;
                    lmax = lc;
                    UpdateDisp(lpos)
                } else {
                    AllClear()
                }
            }
        } else {
            if (g.test(activeInput) == true) {
                unitsClear();
                unitsValueClear()
            } else {
                if ((activeInput == "hex_box") || (activeInput == "dec_box") || (activeInput == "oct_box")) {
                    clearBases()
                } else {
                    if (activeInput == "ecalc_precision") {
                        calcPrec = "";
                        updatePrecision()
                    } else {
                        if (activeInput == "dToFInput") {
                            document.getElementById(activeInput).value = "";
                            calc()
                        } else {
                            if (activeInput != "") {
                                document.getElementById(activeInput).value = ""
                            }
                        }
                    }
                }
            }
        }
    }
}
function clearAll() {
    document.getElementById("ecalc_stack0").value = "0";
    document.getElementById("ecalc_stack1").innerHTML = "";
    document.getElementById("ecalc_stack2").innerHTML = "";
    document.getElementById("ecalc_stack3").innerHTML = "";
    document.getElementById("ecalc_stack4").innerHTML = ""
}
function popStack(q) {
    var g = "";
    var l = "";
    if ((SModeValue == 1) && (q == 2 || q == 4)) {
        g = document.getElementById("ecalc_stack" + q.toString()).innerHTML;
        var m = cNum(0, 0);
        if (q == 2) {
            m = stk[lpos - 1]
        } else {
            if (q == 4) {
                m = stk[lpos - 2]
            }
        }
        if (SCoordValue == 1) {
            l = cShowPair(m, 15)
        } else {
            if ((SCoordValue == 2)) {
                if (m.i == 0) {
                    l = cShowPair(m, 15)
                } else {
                    l = cShowPolar(m, 15)
                }
            }
        }
        g = replaceAll(g, "Ans", l)
    } else {
        var o = cNum(0, 0);
        if (SModeValue == 1) {
            if (q == 1) {
                o = stk[lpos]
            } else {
                if (q == 3) {
                    o = stk[lpos - 1]
                }
            }
        } else {
            o = stk[lpos - parseFloat(q) + 1]
        }
        if (SCoordValue == 1) {
            g = cShowPair(o, 15)
        } else {
            if (SCoordValue == 2) {
                if (o.i == 0) {
                    g = cShowPair(o, 15)
                } else {
                    g = cShowPolar(o, 15)
                }
            }
        }
    }
    var h = new RegExp("unitInput");
    if ((h.test(activeInput) == true) || (activeInput == "dToFInput")) {
        document.getElementById(activeInput).value = ""
    }
    numPressed(g);
    fromNumPress = false;
    activeInput = "ecalc_stack0";
    showCursor()
}
function changeSign() {
    var l = (document.getElementById("ecalc_stack0").value).split("");
    var h = "";
    var g = "";
    var m = false;
    for (i = (l.length - 1); i >= 0; i--) {
        g = l[i];
        if (m != true) {
            if (g == "-") {
                m = true
            } else {
                if (g == "+") {
                    m = true;
                    h = "-" + h
                } else {
                    if (g == "E") {
                        h = "E-" + h;
                        m = true
                    } else {
                        if (g == "e") {
                            h = "e-" + h;
                            m = true
                        } else {
                            if (g == "\u2220") {
                                h = "\u2220-" + h;
                                m = true
                            } else {
                                if (g == ",") {
                                    h = ",-" + h;
                                    m = true
                                } else {
                                    if (g == "(") {
                                        h = "(-" + h;
                                        m = true
                                    } else {
                                        h = g + h
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            h = l[i] + h
        }
    }
    if (m == false) {
        h = "-" + h
    }
    document.getElementById("ecalc_stack0").value = h
}
function closePal() {
    activeInput = "ecalc_stack0";
    if (dfState == "true") {
        toggle_df()
    }
    if (window.widget) {
        slideClosed()
    } else {
        if (mio) {
            document.getElementById("palette_wrap").style.display = "none";
            mio_autoShape_apply()
        } else {
            closePalAnim()
        }
    }
    palSlide = 0
}
function openPal() {
    if (mio) {
        document.getElementById("palette_wrap").style.display = "block";
        mio_autoShape_apply()
    } else {
        if (window.widget) {
            slideOpen()
        } else {
            openPalAnim()
        }
    }
    palSlide = 1
}
function closePalAnim() {
    var g = {
        type: "left",
        from: 367,
        to: 0,
        step: -60,
        delay: 30
    };
    var h = {
        type: "width",
        from: 734,
        to: 391,
        step: -60,
        delay: 30
    };
    var l = {
        type: "width",
        from: 734,
        to: 357,
        step: -60,
        delay: 30
    };
    $fx("#palette_wrap").fxAdd(g).fxRun();
    $fx("#ecalc_wrapW").fxAdd(h).fxRun();
    $fx("#navBar_center").fxAdd(l).fxRun()
}
function openPalAnim() {
    var g = {
        type: "left",
        from: 0,
        to: 367,
        step: 60,
        delay: 30
    };
    var h = {
        type: "width",
        from: 391,
        to: 734,
        step: 60,
        delay: 30
    };
    var l = {
        type: "width",
        from: 357,
        to: 734,
        step: 60,
        delay: 30
    };
    $fx("#palette_wrap").fxAdd(g).fxRun();
    $fx("#ecalc_wrapW").fxAdd(h).fxRun();
    $fx("#navBar_center").fxAdd(l).fxRun()
}
function togglePalSlide() {
    if (palSlide == 1) {
        closePal();
        palSlide = 0
    } else {
        if (palSlide == 0) {
            openPal();
            palSlide = 1
        }
    }
}
function restorePal() {
    if (palSlide == 1) {
        openPal()
    } else {
        if (palSlide == 0) {
            closePal()
        }
    }
}
function hoverOver() {
    document.getElementById("hover_bar").style.backgroundColor = "b3b3b3"
}
function hoverOut() {
    document.getElementById("hover_bar").style.backgroundColor = "c5c5c5"
}
function showUnitArrow(h) {
    var g = "";
    for (i = 0; i <= 11; i++) {
        g = "arrowWrap";
        g += i;
        document.getElementById(g).style.display = "none"
    }
    document.getElementById("arrowWrap" + h).style.display = "block"
}
function formatNumUC(g, h) {
    var m = 0;
    var l = 0;
    if (g == 0) {
        m = "0"
    } else {
        var l = Math.floor(Math.log(Math.abs(g)) / Math.log(10));
        if (l >= 7 || l <= -4) {
            if ((l >= 100) || (l <= -100)) {
                m = g.toExponential(h - 1)
            } else {
                m = g.toExponential(h)
            }
        } else {
            m = g.toFixed(h - l + 3)
        }
    }
    m = stripZeros(m);
    return m
}
function switchCat(g) {
    var h = "";
    h = "unit_cat";
    for (i = 0; i <= 11; i++) {
        h = "unit_cat";
        h += i;
        document.getElementById(h).className = ""
    }
    h = "unit_cat" + g;
    document.getElementById(h).className = "stickUnit_cat" + g;
    unitCat = g;
    updateUnits(g);
    unitsClear();
    unitsDisp(-1);
    activeInput = "ecalc_stack0";
    setCaretToEnd();
    return true
}
function sciPrefix(g) {
    g = g.replace(/([0-9])P(?![a-zA-Z])/g, "$1e15");
    g = g.replace(/([0-9])T(?![a-zA-Z])/g, "$1e12");
    g = g.replace(/([0-9])G(?![a-zA-Z])/g, "$1e9");
    g = g.replace(/([0-9])M(?![a-zA-Z])/g, "$1e6");
    g = g.replace(/([0-9])k(?![a-zA-Z])/g, "$1e3");
    g = g.replace(/([0-9])m(?![a-zA-Z])/g, "$1e-3");
    g = g.replace(/([0-9])u(?![a-zA-Z])/g, "$1e-6");
    g = g.replace(/([0-9])n(?![a-zA-Z])/g, "$1e-9");
    g = g.replace(/([0-9])p(?![a-zA-Z])/g, "$1e-12");
    g = g.replace(/([0-9])f(?![a-zA-Z])/g, "$1e-15");
    return g
}
function unitsConvert(g) {
    var h = document.getElementById("unitInput" + g).value;
    h = sciPrefix(h);
    if (h == "") {
        unitsValueClear();
        unitsClear()
    } else {
        if (h != unitDisp[unitCat][g]) {
            if (unitCat == 9) {
                for (i = 0; i <= 11; i++) {
                    if (unitType[unitCat][i] != undefined) {
                        if (i != g) {
                            unitStore[unitCat][i] = formatNumNice((tempConvt(parseFloat(h), unitType[unitCat][g][3], unitType[unitCat][i][3])), 12)
                        } else {
                            unitStore[unitCat][g] = h
                        }
                    }
                }
            } else {
                for (i = 0; i <= 11; i++) {
                    if (unitType[unitCat][i] != undefined) {
                        if (i != g) {
                            unitStore[unitCat][i] = formatNumNice(parseFloat((unitType[unitCat][g][3] / unitType[unitCat][i][3]) * (h)), 12)
                        } else {
                            unitStore[unitCat][g] = h
                        }
                    }
                }
            }
            unitsDisp(g)
        }
    }
}
function unitsDisp(g) {
    var h = false;
    var l;
    for (i = 0; i <= 11; i++) {
        if ((unitType[unitCat][i] != undefined) && (unitStore[unitCat][i] != "")) {
            h = true;
            l = formatNumUC(parseFloat((unitStore[unitCat][i])), 7);
            unitDisp[unitCat][i] = l;
            if (i != g) {
                document.getElementById("unitInput" + i).value = l
            }
        } else {
            unitDisp[unitCat][i] = ""
        }
    }
    if (h) {
        unitsBgColor(unitStates[unitCat])
    } else {
        unitsBgColor(-1)
    }
}
function unitsFormat(h) {
    var g = document.getElementById("unitInput" + h).value;
    g = sciPrefix(g);
    if ((g != "") && (fromNumPress == false)) {
        document.getElementById("unitInput" + h).value = formatNumUC(parseFloat(g), 7)
    }
    fromNumPress = false
}
function unitsClear() {
    for (i = 0; i <= 11; i++) {
        document.getElementById("unitInput" + i).value = ""
    }
}
function unitsValueClear() {
    for (i = 0; i <= 11; i++) {
        unitStore[unitCat][i] = "";
        unitDisp[unitCat][i] = ""
    }
}
function unitConvInit() {
    for (i = 0; i < unitStore.length; i++) {
        for (j = 0; j < unitStore[i].length; j++) {
            unitStore[i][j] = "";
            unitDisp[i][j] = ""
        }
    }
    switchCat(0)
}
function updateUnits(h) {
    var l;
    var g;
    l = unitType[unitCat].length;
    for (i = 0; i <= 11; i++) {
        if (unitType[unitCat][i] != undefined) {
            document.getElementById("unitBox" + i).style.visibility = "visible";
            document.getElementById("unitBigText" + i).innerHTML = unitType[unitCat][i][0];
            g = unitType[unitCat][i][1];
            if (g == "") {
                document.getElementById("unitSmallText2_" + i).style.bottom = "8px"
            } else {
                document.getElementById("unitSmallText2_" + i).style.bottom = "3px"
            }
            document.getElementById("unitSmallText1_" + i).innerHTML = g;
            document.getElementById("unitSmallText2_" + i).innerHTML = unitType[unitCat][i][2];
            document.getElementById("unitInput" + i).value = "";
            document.getElementById("unitLine" + i).style.display = "block"
        } else {
            document.getElementById("unitBox" + i).style.visibility = "hidden";
            document.getElementById("unitBigText" + i).innerHTML = "";
            document.getElementById("unitSmallText1_" + i).innerHTML = "";
            document.getElementById("unitSmallText2_" + i).innerHTML = "";
            document.getElementById("unitSmallText2_" + i).innerHTML = "";
            document.getElementById("unitLine" + i).style.display = "none"
        }
    }
    g = unitType[unitCat].length;
    g = g - 1;
    document.getElementById("unitLine" + g).style.display = "none";
    document.getElementById("unitTitle").innerHTML = "Unit Conversion:&nbsp;" + unitTitleText[h]
}
function unitPopValue(g) {
    activeInput = "ecalc_stack0";
    showCursor();
    numPressed(formatNumNice(parseFloat(unitStore[unitCat][g]), 11))
}
function changeStyle(styleIndex, init) {
    colorStyle = styleIndex;
    if (compMode != "webV") {
        document.getElementById("stylesheet").href = "default" + styleIndex + ".css"
    } else {
        //document.getElementById("stylesheet").href = "/tools/calculator/default" + styleIndex + ".css"
    }
    smallColorBackgroundSet(styleIndex);
    color4 = eval("color" + styleIndex + "_4");
    color3 = eval("color" + styleIndex + "_3");
    color2 = eval("color" + styleIndex + "_2");
    color1 = eval("color" + styleIndex + "_1");
    hoverColor = color2;
    cellColorHot = color1;
    cellColorHover = hoverColor;
    if (unitStates[unitCat] >= 0) {
        unitsBgColor(unitStates[unitCat])
    }
    document.getElementById("ecalc_hovr1").style.backgroundColor = color2;
    document.getElementById("ecalc_hovr2").style.backgroundColor = color2;
    document.getElementById("ecalc_hovr3").style.backgroundColor = color2;
    document.getElementById("ecalc_hovr4").style.backgroundColor = color2;
    if (window.widget) {
        savePrefs()
    }
}
function smallColorInit() {
    document.getElementById("smallColor1").style.backgroundColor = color1_2;
    document.getElementById("smallColor2").style.backgroundColor = color2_2;
    document.getElementById("smallColor3").style.backgroundColor = color3_2;
    document.getElementById("smallColor4").style.backgroundColor = color4_2;
    document.getElementById("smallColor5").style.backgroundColor = color5_2;
    document.getElementById("smallColor6").style.backgroundColor = color6_2;
    document.getElementById("smallColor1").style.border = "2px solid " + color1_1;
    document.getElementById("smallColor2").style.border = "2px solid " + color2_1;
    document.getElementById("smallColor3").style.border = "2px solid " + color3_1;
    document.getElementById("smallColor4").style.border = "2px solid " + color4_1;
    document.getElementById("smallColor5").style.border = "2px solid " + color5_1;
    document.getElementById("smallColor6").style.border = "2px solid " + color6_1
}
function smallColorBackgroundSet(index) {
    for (i = 1; i <= 6; i++) {
        document.getElementById("smallColor" + i.toString()).style.backgroundColor = eval("color" + i + "_2")
    }
    document.getElementById("smallColor" + index).style.backgroundColor = eval("color" + index + "_1")
}
function smallColorHover(index, color) {
    index.style.backgroundColor = eval("color" + color + "_1")
}
function smallColorOut(index, color) {
    if (color != colorStyle) {
        index.style.backgroundColor = eval("color" + color + "_2")
    }
}
function setColorThemes() {
    document.getElementById("colorA1").style.backgroundColor = color1_1;
    document.getElementById("colorA2").style.backgroundColor = color1_2;
    document.getElementById("colorA3").style.backgroundColor = color1_3;
    document.getElementById("colorB1").style.backgroundColor = color2_1;
    document.getElementById("colorB2").style.backgroundColor = color2_2;
    document.getElementById("colorB3").style.backgroundColor = color2_3;
    document.getElementById("colorC1").style.backgroundColor = color3_1;
    document.getElementById("colorC2").style.backgroundColor = color3_2;
    document.getElementById("colorC3").style.backgroundColor = color3_3;
    document.getElementById("colorD1").style.backgroundColor = color4_1;
    document.getElementById("colorD2").style.backgroundColor = color4_2;
    document.getElementById("colorD3").style.backgroundColor = color4_3;
    document.getElementById("colorE1").style.backgroundColor = color5_1;
    document.getElementById("colorE2").style.backgroundColor = color5_2;
    document.getElementById("colorE3").style.backgroundColor = color5_3;
    document.getElementById("colorF1").style.backgroundColor = color6_1;
    document.getElementById("colorF2").style.backgroundColor = color6_2;
    document.getElementById("colorF3").style.backgroundColor = color6_3
}
function windowButtonHover(g) {
    g.className = g.id + "Hover"
}
function windowButtonOut(g) {
    g.className = g.id + "Out"
}
function makeBinExp(l, g, h) {
    if (l == "Pow") {
        l = "^"
    }
    if (g != "") {
        expList[lc] = inList[lc] + l + g
    } else {
        if (h == 1) {
            expList[lc] = inList[lc - 1] + l + inList[lc]
        } else {
            expList[lc] = inList[lc] + l + inList[lc + 1]
        }
    }
}
function makeUnaryExp(h, g) {
    if (h == "Exp") {
        h = "e^"
    }
    if (g != "") {
        expList[lc] = h + "(" + g + ")"
    } else {
        expList[lc] = h + "(" + inList[lc] + ")"
    }
}
function stackHover(g) {
    document.getElementById("ecalc_hovr" + g).style.visibility = "visible"
}
function stackOut(g) {
    document.getElementById("ecalc_hovr" + g).style.visibility = "hidden"
}
function hookEvent(h, g, l) {
    if (typeof(h) == "string") {
        h = document.getElementById(h)
    }
    if (h == null) {
        return
    }
    if (h.addEventListener) {
        if (g == "mousewheel") {
            h.addEventListener("DOMMouseScroll", l, false)
        }
        h.addEventListener(g, l, false)
    } else {
        if (h.attachEvent) {
            h.attachEvent("on" + g, l)
        }
    }
}
function MouseWheel(h) {
    h = h ? h : window.event;
    var g = h.detail ? h.detail * -1 : h.wheelDelta / 40;
    return cancelEvent(h)
}
function cancelEvent(g) {
    g = g ? g : window.event;
    if (g.stopPropagation) {
        g.stopPropagation()
    }
    if (g.preventDefault) {
        g.preventDefault()
    }
    g.cancelBubble = true;
    g.cancel = true;
    g.returnValue = false;
    return false
}
function moveObject(l) {
    l = l ? l : window.event;
    var g = l.detail ? l.detail : l.wheelDelta;
    var h = l.detail ? l.detail * -1 : l.wheelDelta / 120;
    if (h >= 1) {
        stackUp()
    } else {
        if (h <= -1) {
            stackDown()
        }
    }
    cancelEvent(l)
}
function solverHover(h, g) {
    document.getElementById(h + g).style.backgroundColor = color2
}
function solverOut(h, g) {
    document.getElementById(h + g).style.backgroundColor = "transparent"
}
function updatePrecision() {
    if (menuState != "false") {
        document.getElementById("ecalc_precision").value = calcPrec
    }
    if (calcPrec > 13) {
        document.getElementById("ecalc_precision").value = 13;
        calcPrec = 13
    } else {
        if (calcPrec === "") {
            document.getElementById("ecalc_precision").value = 3;
            calcPrec = 3
        } else {
            if (calcPrec <= 0) {
                document.getElementById("ecalc_precision").value = 0;
                calcPrec = 0
            }
        }
    }
    if (window.widget) {
        savePrefs()
    }
    UpdateDisp(lpos)
}
function unitsBgColor(g) {
    unitStates[unitCat] = g;
    for (i = 0; i <= 11; i++) {
        if ((unitType[unitCat][i] != undefined) && (i != g)) {
            document.getElementById("unitBox" + i).style.backgroundColor = "#FFFFFF";
            document.getElementById("unitInput" + i).style.backgroundColor = "#FFFFFF";
            document.getElementById("unitPop" + i).className = "unitPopWhite"
        }
    }
    if (g >= 0) {
        document.getElementById("unitBox" + g).style.backgroundColor = color4;
        document.getElementById("unitInput" + g).style.backgroundColor = color4;
        document.getElementById("unitPop" + g).className = "unitPopColor"
    }
}
function showCursor() {
    if ((dStack < 2) || (dFloat == 3)) {
        if (activeInput != "") {
            document.getElementById(activeInput).focus();
            if ((isIE) && (menuState != "false")) {
                var g = window.event.srcElement.tagName.toLowerCase();
                if ((g != "a") && (g != "input")) {
                    setCaretToEnd()
                }
            }
        }
        fromNumPress = false
    }
}
function setActiveInput(g) {
    activeInput = g
}
function insertAtCursor(l) {
    myField = document.getElementById(activeInput);
    if (myField.selectionStart || myField.selectionStart == "0") {
        var h = myField.selectionStart;
        var g = myField.selectionEnd;
        var m = myField.scrollTop;
        myField.value = myField.value.substring(0, h) + l + myField.value.substring(g, myField.value.length);
        myField.focus();
        myField.selectionStart = h + l.length;
        myField.selectionEnd = h + l.length;
        myField.scrollTop = m
    } else {
        if (document.selection) {
            sel = document.selection.createRange();
            sel.text = l;
            myField.focus();
            sel.select()
        } else {
            myField.value += l;
            myField.focus()
        }
    }
    if (/Firefox[\/\s](\d+\.\d+)/.test(navigator.userAgent)) {
        simKey()
    }
}
function simKey() {
    myField = document.getElementById(activeInput);
    if (window.KeyEvent) {
        var g = document.createEvent("KeyboardEvent");
        g.initKeyEvent("keypress", true, true, null, false, false, false, false, 27, 0)
    }
    myField.dispatchEvent(g)
}
function backAtCursor() {
    myField = document.getElementById(activeInput);
    if (myField.selectionStart || myField.selectionStart == "0") {
        var l = myField.selectionStart;
        var h = myField.selectionEnd;
        myField.value = myField.value.substring(0, l - 1) + myField.value.substring(h, myField.value.length);
        myField.selectionStart = l - 1;
        myField.selectionEnd = l - 1;
        myField.focus()
    } else {
        if (document.selection) {
            myField.focus();
            sel = document.selection.createRange();
            if (sel.text.length > 0) {
                sel.text = ""
            } else {
                sel.moveStart("character", -1);
                sel.text = ""
            }
            sel.select()
        } else {
            myField.value = myField.value.substr(0, (myField.value.length - 1));
            myField.focus()
        }
    }
    var g = new RegExp("unitInput");
    if (g.test(activeInput) == true) {
        unitsConvert(unitStates[unitCat])
    }
    if (/Firefox[\/\s](\d+\.\d+)/.test(navigator.userAgent)) {
        simKey()
    }
}
function setCaretToEnd() {
    obj = document.getElementById(activeInput);
    pos = obj.value.length;
    if (obj.selectionStart) {
        obj.focus();
        obj.setSelectionRange(pos, pos)
    } else {
        if (obj.createTextRange) {
            var g = obj.createTextRange();
            g.move("character", pos);
            g.select()
        }
    }
}
function enableD() {
    activeInput = "";
    document.getElementById("ecalc_screen").style.display = "none";
    document.getElementById("ecalc_demo").style.display = "block";
    disableCont();
    setTimeout("enableCont();", 2000)
}
function restD() {
    disableCont();
    document.getElementById("ecalc_demo").style.display = "none";
    document.getElementById("ecalc_screen").style.display = "block";
    dStack = 0;
    activeInput = "ecalc_stack0"
}
function disableCont() {
    document.getElementById("ecalcCont").style.display = "none"
}
function enableCont() {
    document.getElementById("ecalcCont").style.display = "block"
}
function hideFocusBorders() {
    var h = document.getElementsByTagName("a");
    if (!h) {
        return
    }
    for (var g = 0; g != h.length; g++) {
        h[g].onfocus = function l() {
            this.hideFocus = true
        }
    }
}
function disableOnSelectStart() {
    thisTagName = window.event.srcElement.tagName.toLowerCase();
    if (thisTagName != "input" && thisTagName != "textarea" && thisTagName != "select") {
        window.event.returnValue = false
    }
}
$(document).ready(function(){
if (compMode != "webV") {
    setTimeout(function() {
        loadXMLDoc(versionCheckURL, "checkVersion")
    }, 100)
}
inList = new Array();
expList = new Array();
stk = new Array();
for (i = -DispHgt; i < 1; i++) {
    inList[i] = "<br>";
    expList[i] = "";
    stk[i] = Zero
}
if (compMode == "webV") {
    initGUI()
};
});
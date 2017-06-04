def kmp(s):
	p = [-1]
	k = -1
	for c in s:
		while k >= 0 and s[k] != c:
			k = p[k]
		k += 1
		p.append(k)
	return p

def period(s):
	k = len(s) - kmp(s)[-1]
	if len(s) % k == 0:
		return k
	return len(s)

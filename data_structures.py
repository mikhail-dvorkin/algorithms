#!/usr/bin/env python3
import random


class Dsu:
	def __init__(self, n):
		self.r = random.Random(1)
		self.p = [i for i in range(n)]

	def get(self, v):
		if self.p[v] == v:
			return v
		self.p[v] = self.get(self.p[v])
		return self.p[v]

	def unite(self, v, u):
		v, u = self.get(v), self.get(u)
		if self.r.getrandbits(1):
			v, u = u, v
		self.p[v] = u

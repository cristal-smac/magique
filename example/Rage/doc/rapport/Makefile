TEXI2DVI=texi2dvi
DVIPS=dvips

FILES=rapport.texi

all: $(FILES)
	@for i in $(FILES); do \
		$(TEXI2DVI) $$i; \
	done
	@$(DVIPS) -o rapport.ps rapport.dvi

clean:
	rm -f *.aux *.log *.dvi *.ps *.pdf *~ *.toc *.cp *.fn *.ky *.pg *.tp *.vr *.bak

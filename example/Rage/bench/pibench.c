#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

int main(void) 
{
	char *argv[] = { "java", "PiSingle", NULL, NULL, NULL };
	char ntask[10], niter[10];
	int i, j;
	time_t start, end;
        pid_t pid;
	FILE * out;

	argv[2] = ntask;
	argv[3] = niter;

	out = fopen("bench", "w");

	for(i = 1000; i < 10000; i += 1000) {	       
		sprintf(argv[2], "%d", i);

		for(j = 1000; j < 1000000; j += 1000) {
			sprintf(argv[3], "%d", j);
			
			switch(pid = fork()) {

			case -1: 
				perror("fork");
				return -1;

			case 0:
				if (execv("/opt/jdk1.3/bin/java",
					  argv) != 0)
					perror("execv");
				return -1;
				
			default:
				start = time(NULL);
				waitpid(pid, NULL, 0);
				end = time(NULL);
			
				fprintf(out, "%d %d %d\n", i, j, end-start);
				printf("%d %d %d\n", i, j, end-start);
				fflush(out);
				break;
			}
		}
	}	
	
	fclose(out);
	return 0;
}

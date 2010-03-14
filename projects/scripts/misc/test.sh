for p in `echo $PATH | ruby -pe "gsub(/:/, \"\n\")"`
do
	cygpath --windows $p
done


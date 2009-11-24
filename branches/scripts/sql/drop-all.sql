rem DANGEROUS!!!
rem Script to drop all tables in regardless of contraints etc.
rem

rem SET NEWPAGE 0
rem SET SPACE 0
rem SET LINESIZE 80
rem SET PAGESIZE 0
rem SET ECHO OFF
rem SET FEEDBACK OFF
rem SET HEADING OFF
rem SET MARKUP HTML OFF
rem SET ESCAPE \
rem SPOOL DELETEME.SQL
select 'drop table ', table_name, 'cascade constraints \;' from user_tables;
rem SPOOL OFF
@DELETEME
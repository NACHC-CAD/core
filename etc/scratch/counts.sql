select 'demo' as tbl, count(*) as cnt from prj_grp_womens_health_demo.Demographics union all
select 'dx' as tbl, count(*) as cnt from prj_grp_womens_health_dx.Diagnosis union all
select 'enc' as tbl, count(*) as cnt from prj_grp_womens_health_enc.Encounter union all
select 'fert' as tbl, count(*) as cnt from prj_grp_womens_health_fert.Fertility union all
select 'obs' as tbl, count(*) as cnt from prj_grp_womens_health_obs.Observation union all
select 'other' as tbl, count(*) as cnt from prj_grp_womens_health_other.Other union all
select 'proc' as tbl, count(*) as cnt from prj_grp_womens_health_proc.Procedure union all
select 'rx' as tbl, count(*) as cnt from prj_grp_womens_health_rx.Rx 


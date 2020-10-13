select 'demo' as tbl, org, count(*) as cnt from prj_grp_womens_health_demo.Demographics group by 1,2  union all
select 'dx' as tbl, org, count(*) as cnt from prj_grp_womens_health_dx.Diagnosis group by 1,2  union all
select 'enc' as tbl, org, count(*) as cnt from prj_grp_womens_health_enc.Encounter group by 1,2  union all
select 'fert' as tbl, org, count(*) as cnt from prj_grp_womens_health_fert.Fertility group by 1,2  union all
select 'obs' as tbl, org, count(*) as cnt from prj_grp_womens_health_obs.Observation group by 1,2  union all
select 'other' as tbl, org, count(*) as cnt from prj_grp_womens_health_other.Other group by 1,2  union all
select 'proc' as tbl, org, count(*) as cnt from prj_grp_womens_health_proc.Procedure group by 1,2  union all
select 'rx' as tbl, org, count(*) as cnt from prj_grp_womens_health_rx.Rx group by 1,2
order by 2, 1


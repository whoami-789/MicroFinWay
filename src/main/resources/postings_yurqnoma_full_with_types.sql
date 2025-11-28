-- postings_yurqnoma_full.sql
-- generated at 2025-10-21T07:41:52.914786Z
-- Encoding: UTF-8

-- === TABLE DDL ===
CREATE TABLE IF NOT EXISTS postings_reference (
  id                SERIAL PRIMARY KEY,
  operation_code    VARCHAR(64) NOT NULL,        -- stable code to reference in app logic
  debit_account     VARCHAR(16) NOT NULL,
  credit_account    VARCHAR(16) NOT NULL,
  name_ru           TEXT NOT NULL,
  name_uz           TEXT,
  doc_type          VARCHAR(16) NOT NULL,         -- 'КО','РО','МО','ЖН','Др' etc.
  direction         VARCHAR(16) NOT NULL,         -- 'IN','OUT','INT','RES','MEMO','XFER' etc.
  is_automatic      BOOLEAN NOT NULL DEFAULT TRUE,
  is_active         BOOLEAN NOT NULL DEFAULT TRUE,
  sort_order        INTEGER NOT NULL DEFAULT 100,
  UNIQUE(operation_code, debit_account, credit_account)
);

INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.DISB.CASH','12401','10101','Выдача Краткосрочный микрозайм физ.лицу (наличными)','ЖШга қисқа муддатли микрозайм бериш (нақд)','КО','OUT',TRUE,TRUE,10);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.DISB.BANK','12401','10503','Выдача Краткосрочный микрозайм физ.лицу (с расчётного счёта)','ЖШга қисқа муддатли микрозайм бериш (банк ҳисобидан)','КО','OUT',TRUE,TRUE,20);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.DISB.CASH','12501','10101','Выдача Краткосрочный микрокредит ИП (наличными)','ЯТТга қисқа муддатли микрокредит бериш (нақд)','КО','OUT',TRUE,TRUE,10);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.DISB.BANK','12501','10503','Выдача Краткосрочный микрокредит ИП (с расчётного счёта)','ЯТТга қисқа муддатли микрокредит бериш (банк ҳисобидан)','КО','OUT',TRUE,TRUE,20);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.DISB.CASH','12701','10101','Выдача Краткосрочный микрокредит юр.лицу (наличными)','ЮШга қисқа муддатли микрокредит бериш (нақд)','КО','OUT',TRUE,TRUE,10);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.DISB.BANK','12701','10503','Выдача Краткосрочный микрокредит юр.лицу (с расчётного счёта)','ЮШга қисқа муддатли микрокредит бериш (банк ҳисобидан)','КО','OUT',TRUE,TRUE,20);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.DISB.CASH','14801','10101','Выдача Долгосрочный микрозайм физ.лицу (наличными)','ЖШга узоқ муддатли микрозайм бериш (нақд)','КО','OUT',TRUE,TRUE,10);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.DISB.BANK','14801','10503','Выдача Долгосрочный микрозайм физ.лицу (с расчётного счёта)','ЖШга узоқ муддатли микрозайм бериш (банк ҳисобидан)','КО','OUT',TRUE,TRUE,20);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.DISB.CASH','14901','10101','Выдача Долгосрочный микрокредит ИП (наличными)','ЯТТга узоқ муддатли микрокредит бериш (нақд)','КО','OUT',TRUE,TRUE,10);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.DISB.BANK','14901','10503','Выдача Долгосрочный микрокредит ИП (с расчётного счёта)','ЯТТга узоқ муддатли микрокредит бериш (банк ҳисобидан)','КО','OUT',TRUE,TRUE,20);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.DISB.REV.CASH','10101','12401','Сторно выдачи Краткосрочный микрозайм физ.лицу (наличными)','ЖШга қисқа муддатли микрозайм беришни сторно (нақд)','МО','XFER',FALSE,TRUE,30);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.DISB.REV.BANK','10503','12401','Сторно выдачи Краткосрочный микрозайм физ.лицу (по банку)','ЖШга қисқа муддатли микрозайм беришни сторно (банк)','МО','XFER',FALSE,TRUE,30);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.DISB.REV.CASH','10101','12501','Сторно выдачи Краткосрочный микрокредит ИП (наличными)','ЯТТга қисқа муддатли микрокредит беришни сторно (нақд)','МО','XFER',FALSE,TRUE,30);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.DISB.REV.BANK','10503','12501','Сторно выдачи Краткосрочный микрокредит ИП (по банку)','ЯТТга қисқа муддатли микрокредит беришни сторно (банк)','МО','XFER',FALSE,TRUE,30);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.DISB.REV.CASH','10101','12701','Сторно выдачи Краткосрочный микрокредит юр.лицу (наличными)','ЮШга қисқа муддатли микрокредит беришни сторно (нақд)','МО','XFER',FALSE,TRUE,30);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.DISB.REV.BANK','10503','12701','Сторно выдачи Краткосрочный микрокредит юр.лицу (по банку)','ЮШга қисқа муддатли микрокредит беришни сторно (банк)','МО','XFER',FALSE,TRUE,30);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.DISB.REV.CASH','10101','14801','Сторно выдачи Долгосрочный микрозайм физ.лицу (наличными)','ЖШга узоқ муддатли микрозайм беришни сторно (нақд)','МО','XFER',FALSE,TRUE,30);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.DISB.REV.BANK','10503','14801','Сторно выдачи Долгосрочный микрозайм физ.лицу (по банку)','ЖШга узоқ муддатли микрозайм беришни сторно (банк)','МО','XFER',FALSE,TRUE,30);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.DISB.REV.CASH','10101','14901','Сторно выдачи Долгосрочный микрокредит ИП (наличными)','ЯТТга узоқ муддатли микрокредит беришни сторно (нақд)','МО','XFER',FALSE,TRUE,30);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.DISB.REV.BANK','10503','14901','Сторно выдачи Долгосрочный микрокредит ИП (по банку)','ЯТТга узоқ муддатли микрокредит беришни сторно (банк)','МО','XFER',FALSE,TRUE,30);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.INT.ACCR.NORMAL','16307','42001','Начисление процентов: Начисл. проценты по кратк. микрозаймам ФЛ','Фоиз ҳисоблаш: Қисқа муддатли микрозаймлар бўйича фоизлар','МО','INT',TRUE,TRUE,40);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.INT.ACCR.NORMAL','16309','42101','Начисление процентов: Начисл. проценты по кратк. микрокредитам ИП','Фоиз ҳисоблаш: Қисқа муддатли микрокредитлар бўйича фоизлар','МО','INT',TRUE,TRUE,40);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.INT.ACCR.NORMAL','16309','42301','Начисление процентов: Начисл. проценты по кратк. микрокредитам ЮЛ','Фоиз ҳисоблаш: Қисқа муддатли микрокредитлар (ЮЛ) бўйича фоизлар','МО','INT',TRUE,TRUE,40);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.INT.ACCR.NORMAL','16307','44201','Начисление процентов: Начисл. проценты по долг. микрозаймам ФЛ','Фоиз ҳисоблаш: Узоқ муддатли микрозаймлар бўйича фоизлар','МО','INT',TRUE,TRUE,40);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.INT.ACCR.NORMAL','16309','44301','Начисление процентов: Начисл. проценты по долг. микрокредитам ИП','Фоиз ҳисоблаш: Узоқ муддатли микрокредитлар (ИП) бўйича фоизлар','МО','INT',TRUE,TRUE,40);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.INT.MOVE.OVERDUE','16377','16307','Перенос начисленных процентов в просрочку','Ҳисобланган фоизларни муддати ўтганга ўтказиш','МО','INT',TRUE,TRUE,50);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.INT.MOVE.OVERDUE','16377','16309','Перенос начисленных процентов в просрочку','Ҳисобланган фоизларни муддати ўтганга ўтказиш','МО','INT',TRUE,TRUE,50);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.INT.MOVE.OVERDUE','16377','16309','Перенос начисленных процентов в просрочку','Ҳисобланган фоизларни муддати ўтганга ўтказиш','МО','INT',TRUE,TRUE,50);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.INT.MOVE.OVERDUE','16377','16307','Перенос начисленных процентов в просрочку','Ҳисобланган фоизларни муддати ўтганга ўтказиш','МО','INT',TRUE,TRUE,50);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.INT.MOVE.OVERDUE','16377','16309','Перенос начисленных процентов в просрочку','Ҳисобланган фоизларни муддати ўтганга ўтказиш','МО','INT',TRUE,TRUE,50);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.INT.ACCR.OD','16377','42005','Начисление просроченных процентов','Муддати ўтган фоизларни ҳисоблаш','МО','INT',TRUE,TRUE,55);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.INT.ACCR.OD','16377','42105','Начисление просроченных процентов','Муддати ўтган фоизларни ҳисоблаш','МО','INT',TRUE,TRUE,55);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.INT.ACCR.OD','16377','42305','Начисление просроченных процентов','Муддати ўтган фоизларни ҳисоблаш','МО','INT',TRUE,TRUE,55);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.INT.ACCR.OD','16377','44201','Начисление просроченных процентов','Муддати ўтган фоизларни ҳисоблаш','МО','INT',TRUE,TRUE,55);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.INT.ACCR.OD','16377','44301','Начисление просроченных процентов','Муддати ўтган фоизларни ҳисоблаш','МО','INT',TRUE,TRUE,55);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.INT.RETURN.NORMAL','16307','16377','Возврат просроченных процентов в нормальные','Муддати ўтган фоизларни оддийга қайтариш','МО','INT',TRUE,TRUE,56);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.INT.RETURN.NORMAL','16309','16377','Возврат просроченных процентов в нормальные','Муддати ўтган фоизларни оддийга қайтариш','МО','INT',TRUE,TRUE,56);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.INT.RETURN.NORMAL','16309','16377','Возврат просроченных процентов в нормальные','Муддати ўтган фоизларни оддийга қайтариш','МО','INT',TRUE,TRUE,56);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.INT.RETURN.NORMAL','16307','16377','Возврат просроченных процентов в нормальные','Муддати ўтган фоизларни оддийга қайтариш','МО','INT',TRUE,TRUE,56);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.INT.RETURN.NORMAL','16309','16377','Возврат просроченных процентов в нормальные','Муддати ўтган фоизларни оддийга қайтариш','МО','INT',TRUE,TRUE,56);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.INT.REPAY.CASH','10101','16307','Поступление оплаты процентов (касса)','Фоиз тўлови тушуми (касса)','РО','IN',TRUE,TRUE,60);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.INT.REPAY.BANK','10503','16307','Поступление оплаты процентов (банк)','Фоиз тўлови тушуми (банк)','РО','IN',TRUE,TRUE,61);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.INT.REPAY.OD.CASH','10101','16377','Поступление оплаты просроченных процентов (касса)','Муддати ўтган фоизлар тўлови тушуми (касса)','РО','IN',TRUE,TRUE,62);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.INT.REPAY.OD.BANK','10503','16377','Поступление оплаты просроченных процентов (банк)','Муддати ўтган фоизлар тўлови тушуми (банк)','РО','IN',TRUE,TRUE,63);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.INT.REPAY.CASH','10101','16309','Поступление оплаты процентов (касса)','Фоиз тўлови тушуми (касса)','РО','IN',TRUE,TRUE,60);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.INT.REPAY.BANK','10503','16309','Поступление оплаты процентов (банк)','Фоиз тўлови тушуми (банк)','РО','IN',TRUE,TRUE,61);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.INT.REPAY.OD.CASH','10101','16377','Поступление оплаты просроченных процентов (касса)','Муддати ўтган фоизлар тўлови тушуми (касса)','РО','IN',TRUE,TRUE,62);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.INT.REPAY.OD.BANK','10503','16377','Поступление оплаты просроченных процентов (банк)','Муддати ўтган фоизлар тўлови тушуми (банк)','РО','IN',TRUE,TRUE,63);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.INT.REPAY.CASH','10101','16309','Поступление оплаты процентов (касса)','Фоиз тўлови тушуми (касса)','РО','IN',TRUE,TRUE,60);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.INT.REPAY.BANK','10503','16309','Поступление оплаты процентов (банк)','Фоиз тўлови тушуми (банк)','РО','IN',TRUE,TRUE,61);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.INT.REPAY.OD.CASH','10101','16377','Поступление оплаты просроченных процентов (касса)','Муддати ўтган фоизлар тўлови тушуми (касса)','РО','IN',TRUE,TRUE,62);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.INT.REPAY.OD.BANK','10503','16377','Поступление оплаты просроченных процентов (банк)','Муддати ўтган фоизлар тўлови тушуми (банк)','РО','IN',TRUE,TRUE,63);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.INT.REPAY.CASH','10101','16307','Поступление оплаты процентов (касса)','Фоиз тўлови тушуми (касса)','РО','IN',TRUE,TRUE,60);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.INT.REPAY.BANK','10503','16307','Поступление оплаты процентов (банк)','Фоиз тўлови тушуми (банк)','РО','IN',TRUE,TRUE,61);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.INT.REPAY.OD.CASH','10101','16377','Поступление оплаты просроченных процентов (касса)','Муддати ўтган фоизлар тўлови тушуми (касса)','РО','IN',TRUE,TRUE,62);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.INT.REPAY.OD.BANK','10503','16377','Поступление оплаты просроченных процентов (банк)','Муддати ўтган фоизлар тўлови тушуми (банк)','РО','IN',TRUE,TRUE,63);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.INT.REPAY.CASH','10101','16309','Поступление оплаты процентов (касса)','Фоиз тўлови тушуми (касса)','РО','IN',TRUE,TRUE,60);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.INT.REPAY.BANK','10503','16309','Поступление оплаты процентов (банк)','Фоиз тўлови тушуми (банк)','РО','IN',TRUE,TRUE,61);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.INT.REPAY.OD.CASH','10101','16377','Поступление оплаты просроченных процентов (касса)','Муддати ўтган фоизлар тўлови тушуми (касса)','РО','IN',TRUE,TRUE,62);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.INT.REPAY.OD.BANK','10503','16377','Поступление оплаты просроченных процентов (банк)','Муддати ўтган фоизлар тўлови тушуми (банк)','РО','IN',TRUE,TRUE,63);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.PRN.REPAY.CASH','10101','12401','Поступление погашения основной суммы долга (касса)','Асосий қарзни қайтариш тушуми (касса)','РО','IN',TRUE,TRUE,70);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.PRN.REPAY.BANK','10503','12401','Поступление погашения основной суммы долга (банк)','Асосий қарзни қайтариш тушуми (банк)','РО','IN',TRUE,TRUE,71);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.PRN.REPAY.CASH','10101','12501','Поступление погашения основной суммы долга (касса)','Асосий қарзни қайтариш тушуми (касса)','РО','IN',TRUE,TRUE,70);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.PRN.REPAY.BANK','10503','12501','Поступление погашения основной суммы долга (банк)','Асосий қарзни қайтариш тушуми (банк)','РО','IN',TRUE,TRUE,71);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.PRN.REPAY.CASH','10101','12701','Поступление погашения основной суммы долга (касса)','Асосий қарзни қайтариш тушуми (касса)','РО','IN',TRUE,TRUE,70);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.PRN.REPAY.BANK','10503','12701','Поступление погашения основной суммы долга (банк)','Асосий қарзни қайтариш тушуми (банк)','РО','IN',TRUE,TRUE,71);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.PRN.REPAY.CASH','10101','14801','Поступление погашения основной суммы долга (касса)','Асосий қарзни қайтариш тушуми (касса)','РО','IN',TRUE,TRUE,70);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.PRN.REPAY.BANK','10503','14801','Поступление погашения основной суммы долга (банк)','Асосий қарзни қайтариш тушуми (банк)','РО','IN',TRUE,TRUE,71);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.PRN.REPAY.CASH','10101','14901','Поступление погашения основной суммы долга (касса)','Асосий қарзни қайтариш тушуми (касса)','РО','IN',TRUE,TRUE,70);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.PRN.REPAY.BANK','10503','14901','Поступление погашения основной суммы долга (банк)','Асосий қарзни қайтариш тушуми (банк)','РО','IN',TRUE,TRUE,71);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.PRN.MOVE.OD','12405','12401','Перенос основного долга в просрочку','Асосий қарзни муддати ўтганга ўтказиш','МО','XFER',TRUE,TRUE,80);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.PRN.MOVE.OD','12505','12501','Перенос основного долга в просрочку','Асосий қарзни муддати ўтганга ўтказиш','МО','XFER',TRUE,TRUE,80);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.PRN.MOVE.OD','12705','12701','Перенос основного долга в просрочку','Асосий қарзни муддати ўтганга ўтказиш','МО','XFER',TRUE,TRUE,80);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.PRN.TO.COURT','15701','12401','Перевод займа/кредита в судебное разбирательство','Қарзни суд жараёнига ўтказиш','МО','XFER',TRUE,TRUE,85);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.PRN.TO.COURT','15703','12501','Перевод займа/кредита в судебное разбирательство','Қарзни суд жараёнига ўтказиш','МО','XFER',TRUE,TRUE,85);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.PRN.TO.COURT','15707','12701','Перевод займа/кредита в судебное разбирательство','Қарзни суд жараёнига ўтказиш','МО','XFER',TRUE,TRUE,85);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('PENALTY.ACCRUE','16405','45994','Начисление пени/штрафа по просроченным обязательствам','Муддати ўтган мажбуриятлар бўйича жарима/пеня ҳисоблаш','МО','INT',TRUE,TRUE,86);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('PENALTY.REPAY.CASH','10101','16405','Оплата пени (касса)','Жарима тўлови (касса)','РО','IN',TRUE,TRUE,87);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('PENALTY.REPAY.BANK','10503','16405','Оплата пени (банк)','Жарима тўлови (банк)','РО','IN',TRUE,TRUE,88);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('FEE.ACCRUE','29801','45201','Начисление комиссий и платы за услуги (к получению)','Хизмат ва воситачилик ҳақлари ҳисоблаш (олинадиган)','МО','INT',TRUE,TRUE,90);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('FEE.REPAY.CASH','10101','29801','Поступление оплаты комиссий (касса)','Комиссия тўлови тушуми (касса)','РО','IN',TRUE,TRUE,91);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('FEE.REPAY.BANK','10503','29801','Поступление оплаты комиссий (банк)','Комиссия тўлови тушуми (банк)','РО','IN',TRUE,TRUE,92);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.RES.25','56802','12499','Формирование резерва 25% по основному долгу','Асосий қарз бўйича 25% захира ажратиш','МО','RES',TRUE,TRUE,100);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.RES.50','56802','12499','Формирование резерва 50% по основному долгу','Асосий қарз бўйича 50% захира ажратиш','МО','RES',TRUE,TRUE,101);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.RES.100','56802','12499','Формирование резерва 100% по основному долгу','Асосий қарз бўйича 100% захира ажратиш','МО','RES',TRUE,TRUE,102);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.RES.25','56802','12599','Формирование резерва 25% по основному долгу','Асосий қарз бўйича 25% захира ажратиш','МО','RES',TRUE,TRUE,100);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.RES.50','56802','12599','Формирование резерва 50% по основному долгу','Асосий қарз бўйича 50% захира ажратиш','МО','RES',TRUE,TRUE,101);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.RES.100','56802','12599','Формирование резерва 100% по основному долгу','Асосий қарз бўйича 100% захира ажратиш','МО','RES',TRUE,TRUE,102);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.RES.25','56802','12799','Формирование резерва 25% по основному долгу','Асосий қарз бўйича 25% захира ажратиш','МО','RES',TRUE,TRUE,100);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.RES.50','56802','12799','Формирование резерва 50% по основному долгу','Асосий қарз бўйича 50% захира ажратиш','МО','RES',TRUE,TRUE,101);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.RES.100','56802','12799','Формирование резерва 100% по основному долгу','Асосий қарз бўйича 100% захира ажратиш','МО','RES',TRUE,TRUE,102);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.RES.25','56802','14899','Формирование резерва 25% по основному долгу','Асосий қарз бўйича 25% захира ажратиш','МО','RES',TRUE,TRUE,100);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.RES.50','56802','14899','Формирование резерва 50% по основному долгу','Асосий қарз бўйича 50% захира ажратиш','МО','RES',TRUE,TRUE,101);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.RES.100','56802','14899','Формирование резерва 100% по основному долгу','Асосий қарз бўйича 100% захира ажратиш','МО','RES',TRUE,TRUE,102);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.RES.25','56802','14999','Формирование резерва 25% по основному долгу','Асосий қарз бўйича 25% захира ажратиш','МО','RES',TRUE,TRUE,100);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.RES.50','56802','14999','Формирование резерва 50% по основному долгу','Асосий қарз бўйича 50% захира ажратиш','МО','RES',TRUE,TRUE,101);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.RES.100','56802','14999','Формирование резерва 100% по основному долгу','Асосий қарз бўйича 100% захира ажратиш','МО','RES',TRUE,TRUE,102);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.RES.RELEASE','12499','56802','Снижение/восстановление резерва по основному долгу','Асосий қарз бўйича захирани қисқартириш/тиклаш','МО','RES',TRUE,TRUE,103);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.RES.RELEASE','12599','56802','Снижение/восстановление резерва по основному долгу','Асосий қарз бўйича захирани қисқартириш/тиклаш','МО','RES',TRUE,TRUE,103);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.RES.RELEASE','12799','56802','Снижение/восстановление резерва по основному долгу','Асосий қарз бўйича захирани қисқартириш/тиклаш','МО','RES',TRUE,TRUE,103);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.RES.RELEASE','14899','56802','Снижение/восстановление резерва по основному долгу','Асосий қарз бўйича захирани қисқартириш/тиклаш','МО','RES',TRUE,TRUE,103);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.RES.RELEASE','14999','56802','Снижение/восстановление резерва по основному долгу','Асосий қарз бўйича захирани қисқартириш/тиклаш','МО','RES',TRUE,TRUE,103);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.WRITEOFF.VIA.RESERVE.STEP1','12499','12401','Покрытие балансовой стоимости за счёт резерва (шаг 1)','Баланс қийматини захира ҳисобидан қоплаш (1-қадам)','МО','RES',TRUE,TRUE,110);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.WRITEOFF.VIA.RESERVE.STEP2','95413','12401','Списание основного долга на внебалансовый счёт (шаг 2)','Асосий қарзни балансдан чиқариш (мемо) (2-қадам)','МО','MEMO',TRUE,TRUE,111);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.WRITEOFF.VIA.RESERVE.STEP1','12599','12501','Покрытие балансовой стоимости за счёт резерва (шаг 1)','Баланс қийматини захира ҳисобидан қоплаш (1-қадам)','МО','RES',TRUE,TRUE,110);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.WRITEOFF.VIA.RESERVE.STEP2','95413','12501','Списание основного долга на внебалансовый счёт (шаг 2)','Асосий қарзни балансдан чиқариш (мемо) (2-қадам)','МО','MEMO',TRUE,TRUE,111);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.WRITEOFF.VIA.RESERVE.STEP1','12799','12701','Покрытие балансовой стоимости за счёт резерва (шаг 1)','Баланс қийматини захира ҳисобидан қоплаш (1-қадам)','МО','RES',TRUE,TRUE,110);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.WRITEOFF.VIA.RESERVE.STEP2','95413','12701','Списание основного долга на внебалансовый счёт (шаг 2)','Асосий қарзни балансдан чиқариш (мемо) (2-қадам)','МО','MEMO',TRUE,TRUE,111);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.WRITEOFF.VIA.RESERVE.STEP1','14899','14801','Покрытие балансовой стоимости за счёт резерва (шаг 1)','Баланс қийматини захира ҳисобидан қоплаш (1-қадам)','МО','RES',TRUE,TRUE,110);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.WRITEOFF.VIA.RESERVE.STEP2','95413','14801','Списание основного долга на внебалансовый счёт (шаг 2)','Асосий қарзни балансдан чиқариш (мемо) (2-қадам)','МО','MEMO',TRUE,TRUE,111);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.WRITEOFF.VIA.RESERVE.STEP1','14999','14901','Покрытие балансовой стоимости за счёт резерва (шаг 1)','Баланс қийматини захира ҳисобидан қоплаш (1-қадам)','МО','RES',TRUE,TRUE,110);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.WRITEOFF.VIA.RESERVE.STEP2','95413','14901','Списание основного долга на внебалансовый счёт (шаг 2)','Асосий қарзни балансдан чиқариш (мемо) (2-қадам)','МО','MEMO',TRUE,TRUE,111);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.WRITTEN.RECOVERY.MEMO.REV','12401','95413','Восстановление списанного на баланс (мемо оборот)','Ёзиб ташланган қарзни балансга қайтариш (мемо)','МО','MEMO',FALSE,TRUE,115);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.WRITTEN.RECOVERY.CASH','10101','45921','Поступление возмещения списанного (касса)','Ёзиб ташланган қарз бўйича тушум (касса)','РО','IN',TRUE,TRUE,116);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.WRITTEN.RECOVERY.BANK','10503','45921','Поступление возмещения списанного (банк)','Ёзиб ташланган қарз бўйича тушум (банк)','РО','IN',TRUE,TRUE,117);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.WRITTEN.RECOVERY.MEMO.REV','12501','95413','Восстановление списанного на баланс (мемо оборот)','Ёзиб ташланган қарзни балансга қайтариш (мемо)','МО','MEMO',FALSE,TRUE,115);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.WRITTEN.RECOVERY.CASH','10101','45921','Поступление возмещения списанного (касса)','Ёзиб ташланган қарз бўйича тушум (касса)','РО','IN',TRUE,TRUE,116);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.WRITTEN.RECOVERY.BANK','10503','45921','Поступление возмещения списанного (банк)','Ёзиб ташланган қарз бўйича тушум (банк)','РО','IN',TRUE,TRUE,117);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.WRITTEN.RECOVERY.MEMO.REV','12701','95413','Восстановление списанного на баланс (мемо оборот)','Ёзиб ташланган қарзни балансга қайтариш (мемо)','МО','MEMO',FALSE,TRUE,115);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.WRITTEN.RECOVERY.CASH','10101','45921','Поступление возмещения списанного (касса)','Ёзиб ташланган қарз бўйича тушум (касса)','РО','IN',TRUE,TRUE,116);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.WRITTEN.RECOVERY.BANK','10503','45921','Поступление возмещения списанного (банк)','Ёзиб ташланган қарз бўйича тушум (банк)','РО','IN',TRUE,TRUE,117);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.WRITTEN.RECOVERY.MEMO.REV','14801','95413','Восстановление списанного на баланс (мемо оборот)','Ёзиб ташланган қарзни балансга қайтариш (мемо)','МО','MEMO',FALSE,TRUE,115);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.WRITTEN.RECOVERY.CASH','10101','45921','Поступление возмещения списанного (касса)','Ёзиб ташланган қарз бўйича тушум (касса)','РО','IN',TRUE,TRUE,116);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.WRITTEN.RECOVERY.BANK','10503','45921','Поступление возмещения списанного (банк)','Ёзиб ташланган қарз бўйича тушум (банк)','РО','IN',TRUE,TRUE,117);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.WRITTEN.RECOVERY.MEMO.REV','14901','95413','Восстановление списанного на баланс (мемо оборот)','Ёзиб ташланган қарзни балансга қайтариш (мемо)','МО','MEMO',FALSE,TRUE,115);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.WRITTEN.RECOVERY.CASH','10101','45921','Поступление возмещения списанного (касса)','Ёзиб ташланган қарз бўйича тушум (касса)','РО','IN',TRUE,TRUE,116);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.WRITTEN.RECOVERY.BANK','10503','45921','Поступление возмещения списанного (банк)','Ёзиб ташланган қарз бўйича тушум (банк)','РО','IN',TRUE,TRUE,117);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('COLLATERAL.TAKE','94502','96381','Принятие обеспечения (имущество/права) на внебаланс','Гаровни қабул қилиш (мулк/ҳуқуқлар) — мемо','МО','MEMO',TRUE,TRUE,120);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('COLLATERAL.RETURN','96381','94502','Возврат/освобождение обеспечения (внебаланс)','Гаровни қайтариш/озод қилиш (мемо)','МО','MEMO',TRUE,TRUE,121);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('COLLATERAL.SALE.CASH','10101','45909','Выручка от реализации обеспечения (касса)','Гаровни сотишдан тушум (касса)','РО','IN',FALSE,TRUE,122);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('COLLATERAL.SALE.BANK','10503','45909','Выручка от реализации обеспечения (банк)','Гаровни сотишдан тушум (банк)','РО','IN',FALSE,TRUE,123);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.INT.REPAY.BANK.TRANSIT.IN','10509','16307','Поступление процентов безналом (в пути)','Фоизлар тушуми нақдсиз (йўлда)','РО','IN',FALSE,TRUE,130);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.PRN.REPAY.BANK.TRANSIT.IN','10509','12401','Поступление основного долга безналом (в пути)','Асосий қарз тушуми нақдсиз (йўлда)','РО','IN',FALSE,TRUE,131);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_KS_FL.BANK.TRANSIT.CLEAR','10503','10509','Зачисление средств из «в пути» на расчётный счёт','Йўлдагидан ҳисоб рақамига ўтказиш','МО','XFER',FALSE,TRUE,132);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.INT.REPAY.BANK.TRANSIT.IN','10509','16309','Поступление процентов безналом (в пути)','Фоизлар тушуми нақдсиз (йўлда)','РО','IN',FALSE,TRUE,130);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.PRN.REPAY.BANK.TRANSIT.IN','10509','12501','Поступление основного долга безналом (в пути)','Асосий қарз тушуми нақдсиз (йўлда)','РО','IN',FALSE,TRUE,131);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_IP.BANK.TRANSIT.CLEAR','10503','10509','Зачисление средств из «в пути» на расчётный счёт','Йўлдагидан ҳисоб рақамига ўтказиш','МО','XFER',FALSE,TRUE,132);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.INT.REPAY.BANK.TRANSIT.IN','10509','16309','Поступление процентов безналом (в пути)','Фоизлар тушуми нақдсиз (йўлда)','РО','IN',FALSE,TRUE,130);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.PRN.REPAY.BANK.TRANSIT.IN','10509','12701','Поступление основного долга безналом (в пути)','Асосий қарз тушуми нақдсиз (йўлда)','РО','IN',FALSE,TRUE,131);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_KS_JL.BANK.TRANSIT.CLEAR','10503','10509','Зачисление средств из «в пути» на расчётный счёт','Йўлдагидан ҳисоб рақамига ўтказиш','МО','XFER',FALSE,TRUE,132);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.INT.REPAY.BANK.TRANSIT.IN','10509','16307','Поступление процентов безналом (в пути)','Фоизлар тушуми нақдсиз (йўлда)','РО','IN',FALSE,TRUE,130);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.PRN.REPAY.BANK.TRANSIT.IN','10509','14801','Поступление основного долга безналом (в пути)','Асосий қарз тушуми нақдсиз (йўлда)','РО','IN',FALSE,TRUE,131);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MS_LS_FL.BANK.TRANSIT.CLEAR','10503','10509','Зачисление средств из «в пути» на расчётный счёт','Йўлдагидан ҳисоб рақамига ўтказиш','МО','XFER',FALSE,TRUE,132);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.INT.REPAY.BANK.TRANSIT.IN','10509','16309','Поступление процентов безналом (в пути)','Фоизлар тушуми нақдсиз (йўлда)','РО','IN',FALSE,TRUE,130);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.PRN.REPAY.BANK.TRANSIT.IN','10509','14901','Поступление основного долга безналом (в пути)','Асосий қарз тушуми нақдсиз (йўлда)','РО','IN',FALSE,TRUE,131);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('MK_LS_IP.BANK.TRANSIT.CLEAR','10503','10509','Зачисление средств из «в пути» на расчётный счёт','Йўлдагидан ҳисоб рақамига ўтказиш','МО','XFER',FALSE,TRUE,132);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('BRANCH.DUE.FUNDS.IN','10503','16102','Поступление средств из Головного офиса/филиала','Бош офис/филиалдан маблағ тушуми','ЖН','IN',FALSE,TRUE,140);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('BRANCH.DUE.FUNDS.OUT','22203','10503','Отправка средств в Головной офис/филиал','Бош офис/филиалга маблағ юбориш','ЖН','OUT',FALSE,TRUE,141);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('NONINT.INCOME.MISC','10101','45994','Поступление прочих безпроцентных доходов (касса)','Бошқа фоизсиз даромадлар тушуми (касса)','РО','IN',FALSE,TRUE,150);
INSERT INTO postings_reference
  (operation_code, debit_account, credit_account, name_ru, name_uz, doc_type, direction, is_automatic, is_active, sort_order)
VALUES
  ('NONINT.EXP.MISC','55995','10101','Прочие безпроцентные расходы (касса)','Бошқа фоизсиз харажатлар (касса)','РО','OUT',FALSE,TRUE,151);
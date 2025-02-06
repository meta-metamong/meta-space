from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity
from typing import List, OrderedDict

class ReservationInfo(BaseModel):
    consId: int
    fctId: int
    totalCount: int

class ReservationInfoList(BaseModel):
    reservation_info: List[ReservationInfo]

app = FastAPI()

@app.post("/recommend/{id}")
def recommend(id: int, data: ReservationInfoList):
    # print(f"Received data: {data}")
    # JSON 데이터를 pandas DataFrame으로 변환 (속성명 맞추기)
    df = pd.DataFrame([{"cons_id": r.consId, "fct_id": r.fctId, "total_count": r.totalCount} for r in data.reservation_info])

    # DataFrame 확인
    print(f"DataFrame:\n{df}")

    # 피벗 테이블 생성 (CONSUMER_ID x FACILITY, 값: TOTAL_COUNT)
    try:
        pivot = df.pivot_table(index='cons_id', columns='fct_id', values='total_count', fill_value=0)
        print(f"Pivot table:\n{pivot}")
    except KeyError as e:
        print(f"KeyError: {e}")
        return {"error": f"Missing key: {e}"}

    # 고객 간 유사도 계산
    consumer_similarity = cosine_similarity(pivot)
    similarity_df = pd.DataFrame(consumer_similarity, index=pivot.index, columns=pivot.index)
    
    # 추천 로직
    def get_recommendations(id):
        # 현재 고객과 유사한 고객 리스트
        similar_consumers = similarity_df[id].sort_values(ascending=False).index[1:]

        # 유사 고객이 구매한 상품 중 현재 고객이 구매하지 않은 상품
        consumer_facilities = set(df[df['cons_id'] == id]['fct_id'])
        similar_facilities = df[df['cons_id'].isin(similar_consumers)]['fct_id']
        recommendations = list(OrderedDict.fromkeys([f for f in similar_facilities if f not in consumer_facilities]))

        # 추천 상품 상위 5개
        return pd.Series(recommendations).value_counts().head(5).index.tolist()

    # 고객 ID가 유효한지 확인하고 추천 결과 생성
    try:
        recommendations = get_recommendations(id)
        print(id, recommendations)
        return {"mem_id": id, "recommended_facilities": recommendations}
    except KeyError:
        raise HTTPException(status_code=404, detail="CONSUMER ID not found")

if __name__ == "__main__":
    import uvicorn

    # FastAPI 앱 실행
    uvicorn.run(app, host="0.0.0.0", port=8000)